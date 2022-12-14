import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class Parser {

    /**
     * Adds the Meta field with the before and after values.
     * @param meta
     * @param field
     * @param before
     * @param after
     */
    private void addToMeta(JSONArray meta, String field, JSONObject before, JSONObject after){
        String fieldBefore = before.getJSONObject("meta").get(field).toString();
        String fieldAfter = after.getJSONObject("meta").get(field).toString();

        if(field.equals("endTime") || field.equals("startTime")){
            fieldBefore = DateFormatter.formatDateFromString(fieldBefore);
            fieldAfter = DateFormatter.formatDateFromString(fieldAfter);
        }

        if(!fieldAfter.equals(fieldBefore)){
            JSONObject title = new JSONObject();
            title.put("field", field);
            title.put("before", fieldBefore);
            title.put("after", fieldAfter);   
            meta.put(title);
        }
    }

    /**
     * Populates Added, Edited and Removed candidates
     * @param candidates
     * @param before
     * @param after
     */
    private void addToCandidates(JSONObject candidates, JSONObject before, JSONObject after){
        JSONArray candidatesEdited = new JSONArray();
        JSONArray candidatesAdded = new JSONArray();
        JSONArray candidatesRemoved = new JSONArray();

        HashMap<Integer, Object> candidatesBefore = this.loadCandidates(before.getJSONArray("candidates"));
        HashMap<Integer, Object> candidatesAfter = this.loadCandidates(after.getJSONArray("candidates"));

        MapDifference<Integer, Object> diff = Maps.difference(candidatesBefore, candidatesAfter);

        for (Integer id : diff.entriesDiffering().keySet()) {
            candidatesEdited.put(new JSONObject("{\"id\":" + id + "}"));    
        }
        for (Integer id : diff.entriesOnlyOnRight().keySet()) {
            candidatesAdded.put(new JSONObject("{\"id\":" + id + "}"));
        }
        for (Integer id : diff.entriesOnlyOnLeft().keySet()) {
            candidatesRemoved.put(new JSONObject("{\"id\":" + id + "}"));    
        }

        candidates.put("edited", candidatesEdited);
        candidates.put("added", candidatesAdded);
        candidates.put("removed", candidatesRemoved);
    }

    /**
     * Loads the incoming candidates into a HashMap for simpler management.
     * @param candidates
     * @return
     */
    private HashMap<Integer, Object> loadCandidates(JSONArray candidates){
        HashMap<Integer, Object> result = new HashMap<Integer, Object>();
        for(int i=0; i<candidates.length(); i++){
            Integer id = (Integer)candidates.getJSONObject(i).get("id");
            String candidateName = candidates.getJSONObject(i).getString("candidateName");
            int extraTime = (Integer)candidates.getJSONObject(i).get("extraTime");

            result.put(id, new Candidate(candidateName, extraTime));
        }

        return result;
    }

    /**
     * Takes 2 JSONObject elements and returns a new JSONObject with the format defined in the <b>readme.md</b> file.
     * @param before
     * @param after
     * @return
     */
    public JSONObject parse(JSONObject before, JSONObject after) {
        JSONObject result = new JSONObject();
        if(before == null || after == null){
            return null;
        }

        // Meta
        JSONArray meta = new JSONArray();
        this.addToMeta(meta, "title", before, after);
        this.addToMeta(meta, "startTime", before, after);
        this.addToMeta(meta, "endTime", before, after);

        // Candidates
        JSONObject candidates = new JSONObject();
        this.addToCandidates(candidates, before, after);

        result.put("meta", meta);
        result.put("candidates", candidates);
        return result;
    }

    /**
     * Encapulates Candidate data
     */
    private class Candidate {
        String candidateName;
        int extraTime;

        public Candidate(String candidateName, int extraTime){
            this.candidateName = candidateName;
            this.extraTime = extraTime;
        }

        @Override
        public boolean equals(Object obj) {
            Candidate candidate = (Candidate)obj;
            return this.candidateName.equals(candidate.candidateName) && this.extraTime == candidate.extraTime;
        }

        public String toString(){
            return "{candidateName= " + candidateName + ", extraTime= " + extraTime +"}";
        }
    }
}
