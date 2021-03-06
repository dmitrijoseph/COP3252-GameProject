package Poker;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;


import java.io.IOException;

public class gameP2PreflopController implements Initializable{

    //Player player1 = Context.getInstance().currentGame().players[0];

    @FXML
    private Label player1Cash;

    @FXML
    private Label player2Cash;

    @FXML
    private ImageView board0;

    @FXML
    private Label round;

    @FXML
    private ImageView board1;

    @FXML
    private ImageView board2;

    @FXML
    private ImageView board3;

    @FXML
    private ImageView board4;

    @FXML
    private Label currentPlayerName;

    @FXML
    private ImageView hand0;

    @FXML
    private ImageView hand1;

    @FXML
    private Button checkButton;

    @FXML
    private Button raiseButton;

    @FXML
    private TextField raiseAmount;

    @FXML
    private Button foldButton;

    @FXML
    private Button allInButton;

    @FXML
    private Button toggleHandButton;

    @FXML
    private Label currentBet;

    @FXML
    private Label errorMSG;

    @FXML
    private Button exitButton;

    @FXML
    private Button restartButton;

    @FXML
    void allIn(MouseEvent event) throws IOException
    {
        int button = Context.getInstance().currentGame().button;
        if(Context.getInstance().currentGame().currentBet == 2)
        {
            Context.getInstance().currentGame().currentBet =0;
        }
        Context.getInstance().currentGame().currentBet += Context.getInstance().currentGame().players[(button+1)%2].getCash();
        Context.getInstance().currentGame().pot += Context.getInstance().currentGame().players[(button+1)%2].getCash();
        Context.getInstance().currentGame().players[(button+1)%2].allIn();
        Context.getInstance().currentGame().players[(button+1)%2].setRespondedTrue();
        if(Context.getInstance().currentGame().players[0].getStatus().equals("ALLIN")  && Context.getInstance().currentGame().players[1].getStatus().equals("ALLIN"))
        {
            Parent gameParent = FXMLLoader.load(getClass().getResource("gameScore.fxml"));
            Scene gameScene = new Scene(gameParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(gameScene);
            window.show();
        }
        else
        {
            Parent gameParent = FXMLLoader.load(getClass().getResource("gameP1Preflop.fxml"));
            Scene gameScene = new Scene(gameParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(gameScene);
            window.show();
        }
    }

    @FXML
    void check(MouseEvent event) throws IOException
    {
        int current =Context.getInstance().currentGame().currentBet;
        int button = Context.getInstance().currentGame().button;
        if(!Context.getInstance().currentGame().hasAllResponded())
        {
            System.out.println(current);
            if (!Context.getInstance().currentGame().players[(button+1)%2].gamble(current))
            {
                errorMSG.setText("You do not have the funds for that");
                return;
            }

            else
            {
                Context.getInstance().currentGame().pot += current;
                Context.getInstance().currentGame().players[(button+1)%2].setRespondedTrue();
                if(!Context.getInstance().currentGame().bettingEnabled())
                {
                    Parent gameParent = FXMLLoader.load(getClass().getResource("gameScore.fxml"));
                    Scene gameScene = new Scene(gameParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(gameScene);
                    window.show();
                }
            }
            if (!Context.getInstance().currentGame().hasAllResponded())
            {
                Parent gameParent = FXMLLoader.load(getClass().getResource("gameP1Preflop.fxml"));
                Scene gameScene = new Scene(gameParent);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(gameScene);
                window.show();
            }
            else
            {
                Parent gameParent = FXMLLoader.load(getClass().getResource("gameP2Flop.fxml"));
                Scene gameScene = new Scene(gameParent);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(gameScene);
                window.show();
            }
        }
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void fold(MouseEvent event) throws IOException
    {
        int button = Context.getInstance().currentGame().button;
        Context.getInstance().currentGame().players[(button+1)%2].fold();
        Context.getInstance().currentGame().players[(button+1)%2].setRespondedTrue();
        Parent gameParent = FXMLLoader.load(getClass().getResource("gameScore.fxml"));
        Scene gameScene = new Scene(gameParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        window.show();
    }

    @FXML
    void raise(MouseEvent event) throws IOException
    {
        int button = Context.getInstance().currentGame().button;
        int current =Context.getInstance().currentGame().currentBet;
        int raise = Integer.parseInt(raiseAmount.getText());
        System.out.println(current + raise);

        if (!Context.getInstance().currentGame().players[(button+1)%2].gamble(current + raise))
        {
            errorMSG.setText("You do not have the funds for that");
        }
        else
        {
            Context.getInstance().currentGame().pot += current + raise;
            Context.getInstance().currentGame().currentBet += raise;
            Context.getInstance().currentGame().setAllRespondedFalse();
            Context.getInstance().currentGame().players[(button+1)%2].setRespondedTrue();
            Parent gameParent = FXMLLoader.load(getClass().getResource("gameP1Preflop.fxml"));
            Scene gameScene = new Scene(gameParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(gameScene);
            window.show();
        }
    }

    @FXML
    void restart(ActionEvent event) throws Exception {
        Context.getInstance().resetGame();
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Scene rootScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(rootScene);
        window.show();
    }

    boolean show = false;
    @FXML
    void toggleHand(MouseEvent event)
    {
        int button = Context.getInstance().currentGame().button;
        show = !show;
        if(show)
        {
            String player1card1 = Context.getInstance().currentGame().players[(button+1)%2].cards.elementAt(0).toString();
            String player1card2 = Context.getInstance().currentGame().players[(button+1)%2].cards.elementAt(1).toString();
            Image card1 = new Image(getClass().getResourceAsStream(player1card1));
            Image card2 = new Image(getClass().getResourceAsStream(player1card2));
            hand0.setImage(card1);
            hand1.setImage(card2);
        }
        else
        {
            hand0.setImage(null);
            hand1.setImage(null);
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        int button = Context.getInstance().currentGame().button;
        Context.getInstance().currentGame().initPreFlop();
        String cbhelp = "Current bet is $";
        String cb = Integer.toString(Context.getInstance().currentGame().currentBet);
        String playerNameHelp ="Current player is ";
        String player1Name = Context.getInstance().currentGame().players[button%2].getName();
        String player2Name = Context.getInstance().currentGame().players[(button+1)%2].getName();
        String plCashHelp = player1Name.concat(" has $");
        String p2CashHelp = player2Name.concat(" has $");

        String p1Cash = plCashHelp.concat(Integer.toString(Context.getInstance().currentGame().players[button%2].getCash()));
        String p2Cash = p2CashHelp.concat(Integer.toString(Context.getInstance().currentGame().players[(button+1)%2].getCash()));
        StringProperty player1CashProperty = new SimpleStringProperty(p1Cash);
        StringProperty player2CashProperty = new SimpleStringProperty(p2Cash);
        StringProperty playerNameProp = new SimpleStringProperty(player2Name);
        currentPlayerName.textProperty().bind(playerNameProp);
        player1Cash.textProperty().bind(player1CashProperty);
        player2Cash.textProperty().bind(player2CashProperty);
        currentBet.textProperty().bind(new SimpleStringProperty(cbhelp+cb));




    }

}

