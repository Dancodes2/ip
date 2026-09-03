package slotbot.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import slotbot.SlotBot;

@Tag("gui")
public class MainWindowTest {
    @BeforeAll
    public static void startToolkit() throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            ready.countDown();
        });
        assertTrue(ready.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void handleUserInput_buttonsAndActions_updateConversationAndExit(@TempDir Path directory)
            throws Exception {
        CountDownLatch closed = new CountDownLatch(1);
        FutureTask<Void> interaction = new FutureTask<>(() -> {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            try {
                Scene scene = new Scene(root);
                scene.getStylesheets().add(Main.class.getResource("/css/main.css").toExternalForm());
                stage.setScene(scene);
                MainWindow controller = loader.getController();
                SlotBot bot = new SlotBot(directory.resolve("tasks.txt"));
                controller.setBot(bot, () -> {
                    stage.close();
                    closed.countDown();
                });
                stage.show();
                root.applyCss();
                root.layout();
                TextField input = (TextField) root.lookup("#userInput");
                Button send = (Button) root.lookup("#sendButton");
                VBox messages = (VBox) root.lookup("#dialogContainer");
                assertEquals(1, messages.getChildren().size());
                input.setText("   ");
                send.fire();
                assertEquals(1, messages.getChildren().size());
                input.setText("todo read book");
                input.fireEvent(new ActionEvent());
                assertEquals(3, messages.getChildren().size());
                assertEquals("", input.getText());
                assertEquals(input, scene.getFocusOwner());
                VBox reply = (VBox) messages.getChildren().get(2);
                assertTrue(((Label) reply.getChildren().get(1)).getText().contains("[T][ ] read book"));
                input.setText("list");
                send.fire();
                assertEquals(5, messages.getChildren().size());
                input.setText("bye extra");
                send.fire();
                assertFalse(bot.isExiting());
                input.setText(" bye ");
                send.fire();
                assertTrue(bot.isExiting());
                assertTrue(input.isDisabled());
                assertTrue(send.isDisabled());
            } catch (Throwable failure) {
                stage.close();
                throw failure;
            }
            return null;
        });
        Platform.runLater(interaction);
        interaction.get(15, TimeUnit.SECONDS);
        assertTrue(closed.await(5, TimeUnit.SECONDS));
    }
}
