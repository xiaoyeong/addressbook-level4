package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final TransactionListPanelHandle personListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final BrowserPanelHandle browserPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        personListPanel = new TransactionListPanelHandle(getChildNode(TransactionListPanelHandle.PERSON_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        browserPanel = new BrowserPanelHandle(getChildNode(BrowserPanelHandle.BROWSER_ID));
    }

    public TransactionListPanelHandle getTransactionListPanel() {
        return personListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public BrowserPanelHandle getBrowserPanel() {
        return browserPanel;
    }
}
