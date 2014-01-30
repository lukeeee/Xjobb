package se.xjobb.scardfeud.JsonGetClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Svempa on 2014-01-30.
 */
public class GameListResponse {
    @SerializedName("responses")
    public List<Response> responses;
}
