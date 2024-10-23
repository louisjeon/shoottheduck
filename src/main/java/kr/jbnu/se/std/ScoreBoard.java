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
    private final int[] scores;
    private final CollectionReference scoresCollection;


    private ScoreBoard() throws IOException, ExecutionException, InterruptedException {
        Firestore db = FirebaseConfig.initialize();
        scoresCollection = db.collection("scores");
        ApiFuture<QuerySnapshot> query = scoresCollection.get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        QueryDocumentSnapshot documentSnapshot = documentSnapshotList.get(0);

        int[] a = new int[5];
        for (int i = 0; i < 5; i++) {
            Object b = documentSnapshot.get(i + 1 + "");
            if (b != null) {
                a[i] = Integer.parseInt(Objects.requireNonNull(b).toString());}
             else  {
                a[i] = 0;
             }
        }
        scores = a;
    }

    public static ScoreBoard getInstance() throws IOException, ExecutionException, InterruptedException {
        if (scoreBoard == null) {
            scoreBoard = new ScoreBoard();
        }
        return scoreBoard;
    }

    public int getScore(int stage) {
        return scores[stage - 1];
    }

    public void setScore(int stage, int score) throws ExecutionException, InterruptedException {
        scores[stage - 1] = score;
        Map<String, Object> data = new HashMap<>();
        data.put(stage + "", score);
        scoresCollection.document("stage").set(data);
      }
}
