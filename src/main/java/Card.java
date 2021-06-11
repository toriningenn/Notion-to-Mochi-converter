
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "card")
public class Card {
    @JsonProperty("content")
    String content;
    @JsonProperty("deck-id")
    String deckId;

    public Card(String content, String deckId) {
        this.content = content;
        this.deckId = deckId;
    }
}
