package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.CardInput;

import java.util.ArrayList;

public class ActionInterpretor {

public void getPlayerDeck(ArrayNode output, Player player, ActionsInput action) {
    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player.getDeck_cards());

}

public void getPlayerHero(ArrayNode output, Player player, ActionsInput action) {
    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player.getHero());
}

public void getPlayerTurn(ArrayNode output, int turn, ActionsInput action) {
    output.addObject().put("command", action.getCommand()).put("output", turn);
}

}
