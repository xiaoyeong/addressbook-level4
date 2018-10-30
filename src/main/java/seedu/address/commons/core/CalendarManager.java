
package seedu.address.commons.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import javafx.application.Platform;

import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.RefreshCalendarEvent;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * Manages user authorization of the Google Calendar service and synchronization of transaction data
 * between the DebtTracker application and Google Calendar.
 */
public class CalendarManager extends ComponentManager {
    private static final Logger logger = LogsCenter.getLogger(CalendarManager.class);
    private static final String APPLICATION_NAME = "Debt Tracker";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CALENDAR_TITLE = "Debt Tracker";


    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/creds/creds.json";

    private static CalendarManager instance;
    private Calendar service;
    private String calendarId;
    private ExecutorService executor;

    private CalendarManager() throws IOException, GeneralSecurityException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        if (CalendarManager.getSavedCredentials(httpTransport) != null) {
            this.service = new Calendar.Builder(httpTransport, JSON_FACTORY,
                    CalendarManager.getSavedCredentials(httpTransport))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            String calendarId = getCalendarIdBySummary(CALENDAR_TITLE);
            if (calendarId == null) {
                createNewCalendar();
                setCalendarAccess();
            } else {
                this.calendarId = calendarId;
            }
        }
        executor = Executors.newFixedThreadPool(1);
    }

    public static CalendarManager getInstance() {
        if (instance == null) {
            try {
                instance = new CalendarManager();
                logger.info("calendar initialized");
            } catch (IOException | GeneralSecurityException | NullPointerException ex) {
                logger.info("Could not initialize calendar");
            }
        }
        return instance;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        GoogleAuthorizationCodeFlow flow = buildFlow(httpTransport);
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static Credential getSavedCredentials(final NetHttpTransport httpTransport) {
        try {
            GoogleAuthorizationCodeFlow flow = buildFlow(httpTransport);
            Credential credential = flow.loadCredential("user");
            if (credential != null && (credential.getRefreshToken() != null
                    || credential.getExpiresInSeconds() == null
                    || credential.getExpiresInSeconds() > 60)) {
                return credential;
            }
        } catch (IOException | NullPointerException ex) {
            logger.info("Error getting user credentials");
        }
        return null;
    }

    /**
     * Builds the Google OAuth 2.0 authorization code flow using the client secret
     * found in {@value #CREDENTIALS_FILE_PATH}.
     * @param httpTransport The network HTTP Transport.
     * @return A GoogleAuthorizationCodeFlow object
     * @throws IOException if the the file containing the client secrets cannot be found.
     */
    private static GoogleAuthorizationCodeFlow buildFlow(final NetHttpTransport httpTransport) throws IOException {
        InputStream in = CalendarManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    public void setReminder(int timePeriod, Transaction transactionToSetReminder) throws IOException {
        List<Event> calendarEvents = getCalendarEvents();
    }

    /**
     * Starts the calendar login process on a new thread and returns true if the user has not already logged in,
     * returns false otherwise.
     */
    public boolean calendarLogin(Model model) {
        if (service == null) {
            Thread t = new Thread(new LoginRunnable(model));
            t.start();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Logs the user out by removing the user's stored credentials.
     * Returns true if the logout is successful, returns false otherwise if the user has already been logged out.
     */
    public boolean calendarLogout() {
        try {
            Files.deleteIfExists(Paths.get("tokens/StoredCredential"));
            this.service = null;
            this.calendarId = null;
            return true;
        } catch (IOException ex) {
            return false;
        }
    }


    public boolean isAuthenticated() {
        return service != null;
    }


    public Calendar getCalendarService() {
        return service;
    }

    /**
     * Creates and inserts a new calendar in Singapore Time for the debt tracker application
     * in the authorized user's account
     */
    private void createNewCalendar() {
        if (service != null) {
            com.google.api.services.calendar.model.Calendar cal = new com.google.api.services.calendar.model.Calendar();
            cal.setSummary("Debt Tracker");
            cal.setTimeZone("Asia/Singapore");
            try {
                this.calendarId = service.calendars().insert(cal).execute().getId();
            } catch (IOException ex) {
                logger.info(ex.getMessage());
            }
        }
    }

    private void setCalendarAccess() {
        AclRule rule = new AclRule();
        AclRule.Scope scope = new AclRule.Scope();
        scope.setType("default");
        rule.setScope(scope).setRole("reader");
        Platform.runLater(() -> {
            try {
                service.acl().insert(this.calendarId, rule).execute();
            } catch (IOException ex) {
                logger.info(ex.getMessage());
            }
        });
    }

    public String getCalendarId() {
        return this.calendarId;
    }

    /**
     * Represents the number of additions and deletions resulting from a calendar sync event
     */
    public class SyncResult {
        final int additions;
        final int deletions;

        SyncResult(int additions, int deletions) {
            this.additions = additions;
            this.deletions = deletions;
        }

        @Override
        public String toString() {
            return this.additions + " additions, " + this.deletions + " deletions.";
        }
    }

    /**
     * Synchronizes the transactions between the calendar and debt tracker application
     * @param model The in-memory model of the debt tracker data
     * @return a SyncResult object representing the number of additions and deletions made to the calendar
     */
    private SyncResult syncCalendarHelper(Model model) {
        try {
            List<Event> eventList = getCalendarEvents();
            List<String> invalidEvents = new LinkedList<>();
            Set<CalendarTransaction> calendarTransactions = new HashSet<>();
            for (Event event : eventList) {
                String name = event.getSummary();
                String description = event.getDescription();
                EventDateTime start = event.getStart();
                EventDateTime end = event.getEnd();
                boolean validEvent = false;
                if (description != null) {
                    String[] details = description.split("\\r?\\n");
                    Set<Tag> tags = new HashSet<>();
                    if (start.equals(end) && (details.length == 5 || details.length == 6)
                            && Name.isValidName(name)
                            && Phone.isValidPhone(details[0]) && Address.isValidAddress(details[1])
                            && Email.isValidEmail(details[2]) && Type.isValidType(details[3])
                            && Amount.isValidAmount(details[4])) {
                        if (details.length == 6) {
                            String[] tagStrings = details[5].split(";");
                            if (Arrays.stream(tagStrings).allMatch(s -> Tag.isValidTagName(s))) {
                                validEvent = true;
                                for (String tagString: tagStrings) {
                                    tags.add(new Tag(tagString));
                                }
                            }
                        } else {
                            validEvent = true;
                        }
                    }
                    if (validEvent) {
                        try {
                            DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String date = targetFormat.format(sourceFormat.parse(end.getDate().toString()));
                            calendarTransactions.add(
                                    new CalendarTransaction(new Transaction(new Type(details[3]),
                                            new Amount(details[4]), new Deadline(date), new Person(new Name(name),
                                            new Phone(details[0]), new Email(details[2]), new Address(details[1]),
                                            tags)), event.getId()));
                        } catch (ParseException ex) {
                            validEvent = false;
                        }
                    }
                    if (!validEvent) {
                        invalidEvents.add(event.getId());
                    }
                }
            }

            ObservableList<Transaction> transactions = model.getFinancialDatabase().getTransactionList();
            Set<CalendarTransaction> appTransactions = new HashSet<>();
            for (Transaction t : transactions) {
                appTransactions.add(new CalendarTransaction(t));
            }

            Set<CalendarTransaction> toRemove = new HashSet<>(calendarTransactions);
            toRemove.removeAll(appTransactions);

            Set<CalendarTransaction> toAdd = new HashSet<>(appTransactions);
            toAdd.removeAll(calendarTransactions);

            executeSyncBatchRequest(toAdd, toRemove, invalidEvents);

            return new SyncResult(toAdd.size(), toRemove.size() + invalidEvents.size());

        } catch (IOException ex) {
            logger.info("unable to sync calendar");
            return null;
        }
    }


    /**
     * Synchronizes the transactions between the calendar and debt tracker application.
     * Runs on a common single-thread executor to prevent concurrent synchronizations
     * @param model The in-memory model of the debt tracker data
     * @return a SyncResult object representing the number of additions and deletions made to the calendar
     */
    public SyncResult syncCalendar(Model model) {
        try {
            if (service != null) {
                SyncResult result = executor.submit(() -> syncCalendarHelper(model)).get();
                return result;
            } else {
                logger.info("calendar not logged in");
                return null;
            }
        } catch (InterruptedException | ExecutionException ex) {
            return null;
        }
    }


    /**
     * Synchronizes the transactions between the calendar and debt tracker application.
     * Runs on a separate thread to avoid blocking UI, in a common single-thread executor
     * to prevent concurrent synchronizations
     */
    public void syncCalendarAsync(Model model) {
        if (service != null) {
            executor.submit(() ->
                    syncCalendarHelper(model)
            );
        } else {
            logger.info("calendar not logged in");
        }
    }

    /**
     * Executes a batch request to the calendar service for synchronizing transactions
     * @param toAdd a set of transactions to add to the calendar
     * @param toRemove a set of transactions to be removed from the calendar
     * @param invalidEvents a list of invalid events to be removed
     */
    private void executeSyncBatchRequest(Set<CalendarTransaction> toAdd, Set<CalendarTransaction> toRemove,
                                         List<String> invalidEvents) throws IOException {
        BatchRequest b = service.batch();
        for (CalendarTransaction add : toAdd) {
            Event e = new Event();
            e.setSummary(add.transaction.getPerson().getName().fullName);
            String description = add.transaction.getPerson().getPhone().value + "\n"
                    + add.transaction.getPerson().getAddress().value + "\n"
                    + add.transaction.getPerson().getEmail().value + "\n"
                    + add.transaction.getType().value + "\n"
                    + add.transaction.getAmount();
            if (!add.transaction.getPerson().getTags().isEmpty()) {
                Set<String> tagStrings = new HashSet<>();
                for (Tag tag : add.transaction.getPerson().getTags()) {
                    tagStrings.add(tag.tagName);
                }
                description += "\n" + String.join(";", tagStrings);
            }
            e.setDescription(description);

            DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = add.transaction.getDeadline().value;
            try {
                String date = targetFormat.format(sourceFormat.parse(dateString));
                EventDateTime start = new EventDateTime().setDate(new DateTime(date));
                EventDateTime end = new EventDateTime().setDate(new DateTime(date));
                e.setStart(start);
                e.setEnd(end);
                e.setReminders(new Event.Reminders().setUseDefault(true));
                service.events().insert(calendarId, e).queue(b, new JsonBatchCallback<>() {
                    @Override
                    public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                        logger.info("Failed to add event to calendar");
                    }

                    @Override
                    public void onSuccess(Event event, HttpHeaders responseHeaders) {
                        logger.info("Added event " + event.getId() + " successfully");
                    }
                });

            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }

        }
        for (CalendarTransaction remove : toRemove) {
            service.events().delete(calendarId, remove.eventId).queue(b, new JsonBatchCallback<>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                    logger.info("Failed to remove event from calendar");
                }

                @Override
                public void onSuccess(Void v, HttpHeaders responseHeaders) {
                    logger.info("Removed event " + remove.eventId + " successfully");
                }

            });

        }

        for (String invalidEventId : invalidEvents) {

            service.events().delete(calendarId, invalidEventId).queue(b, new JsonBatchCallback<>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                    logger.info("Failed to remove event from calendar");
                }

                @Override
                public void onSuccess(Void v, HttpHeaders responseHeaders) {
                    logger.info("Removed event " + invalidEventId + " successfully");
                }

            });

        }
        if (b.size() > 0) {
            b.execute();
            raise(new RefreshCalendarEvent(calendarId));
        }
    }


    private List<Event> getCalendarEvents() throws IOException {

        List<Event> eventList = new LinkedList<>();
        String pageToken = null;
        do {
            Events events = service.events().list(calendarId).setMaxResults(2500).setPageToken(pageToken).execute();
            eventList.addAll(events.getItems());
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return eventList;

    }

    private String getCalendarIdBySummary(String summary) throws IOException {
        if (service == null) {
            return null;
        }
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                if (calendarListEntry.getSummary().equals(summary)) {
                    return calendarListEntry.getId();
                }
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return null;
    }


    /**
     * The login process that is to be executed in a new thread.
     */
    private class LoginRunnable implements Runnable {
        private Model model;
        LoginRunnable(Model model) {
            this.model = model;
        }
        @Override
        public void run() {
            try {
                final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                CalendarManager.getInstance().service = new Calendar.Builder(httpTransport, JSON_FACTORY,
                        CalendarManager.getCredentials(httpTransport))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
                String calendarId = getCalendarIdBySummary("Debt Tracker");
                if (calendarId == null) {
                    createNewCalendar();
                    setCalendarAccess();
                } else {
                    CalendarManager.getInstance().calendarId = calendarId;
                }
                syncCalendarAsync(model);
                raise(new NewResultAvailableEvent("Logged in successfully!"));
            } catch (IOException | GeneralSecurityException | NullPointerException ex) {
                logger.info("Error getting user credentials");
                raise(new NewResultAvailableEvent("Login unsuccessful!"));
            }
        }
    }


    /**
     * Represents a transaction event in the calendar
     */
    private class CalendarTransaction {
        private String eventId;
        private Transaction transaction;

        CalendarTransaction(Transaction transaction) {
            this.transaction = transaction;
        }

        CalendarTransaction(Transaction transaction, String eventId) {
            this.transaction = transaction;
            this.eventId = eventId;
        }


        @Override
        public int hashCode() {
            return transaction.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof CalendarTransaction)) {
                return false;
            }
            CalendarTransaction calTransaction = (CalendarTransaction) other;
            return other == this || this.transaction.equals(calTransaction.transaction);
        }

        @Override
        public String toString() {
            return (eventId == null ? "No EventId " : eventId) + transaction.toString();
        }

    }
}
