package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ScoreBoard {
    private static ScoreBoard scoreBoard;
    private final static int[] scores = new int[5];
    private static final Firestore db;

    static {
        try {
            db = FirebaseConfig.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final CollectionReference scoresCollection = db.collection("scores");;


    private ScoreBoard() throws ExecutionException, InterruptedException {
        System.out.println("ScoreBoard Loading...");
        ApiFuture<QuerySnapshot> query = scoresCollection.get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        QueryDocumentSnapshot documentSnapshot = documentSnapshotList.get(0);

        for (int i = 0; i < 5; i++) {
            Object b = documentSnapshot.get(i + 1 + "");
            if (b != null) {
                scores[i] = Integer.parseInt(Objects.requireNonNull(b).toString());}
             else  {
                scores[i] = 0;
             }
        }
        System.out.println("ScoreBoard Loading Done!!!!!");
    }

    public static ScoreBoard getInstance() throws IOException, ExecutionException, InterruptedException {
        if (scoreBoard == null) {
            scoreBoard = new ScoreBoard();
        }
        return scoreBoard;
    }

    public static int getScore(int stage) {
        return scores[stage - 1];
    }

    public static void setScore(int stage, int score) {
        scores[stage - 1] = score;
        Map<String, Object> data = new HashMap<>();
        data.put(stage + "", score);
        scoresCollection.document("stage").set(data);
      }
}
