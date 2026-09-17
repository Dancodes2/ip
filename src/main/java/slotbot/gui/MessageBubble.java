package slotbot.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Displays one user or SlotBot message in an aligned, wrapping chat bubble.
 */
public class MessageBubble extends HBox {
    private static final double MAXIMUM_CONTENT_WIDTH_RATIO = 0.82;

    /**
     * Creates a message bubble with styling and alignment for its sender.
     *
     * @param text Message to display.
     * @param isUser Whether the user sent the message.
     */
    public MessageBubble(String text, boolean isUser) {
        Label author = createAuthorLabel(isUser);
        Label message = createMessageLabel(text, isUser);
        VBox content = new VBox(5, author, message);

        content.setFillWidth(true);
        content.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        content.getStyleClass().add("message-content");
        content.maxWidthProperty().bind(widthProperty().multiply(MAXIMUM_CONTENT_WIDTH_RATIO));

        setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        setMaxWidth(Double.MAX_VALUE);
        getStyleClass().addAll("message-row", isUser ? "user-row" : "bot-row");
        getChildren().add(content);
    }

    /**
     * Creates the label that identifies a message's sender.
     */
    private Label createAuthorLabel(boolean isUser) {
        Label author = new Label(isUser ? "YOU" : "SLOTBOT");
        author.getStyleClass().addAll("author", isUser ? "user-author" : "bot-author");
        return author;
    }

    /**
     * Creates a wrapping label styled for a user or SlotBot message.
     */
    private Label createMessageLabel(String text, boolean isUser) {
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMinWidth(0);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().addAll("message-text", isUser ? "user-message" : "bot-message");
        return message;
    }
}
