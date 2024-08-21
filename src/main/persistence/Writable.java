package persistence;

import org.json.JSONObject;

public interface Writable {
    //EFFECTS: Returns this as JSON object
    JSONObject toJson();
}
