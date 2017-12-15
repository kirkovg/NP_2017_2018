package mk.ukim.finki.np.lab8.task5;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


abstract class TriviaQuestion {
    private String question;        // Actual question
    private String answer;        // Answer to question
    private int value;            // Point value of question

    public TriviaQuestion(String question, String answer, int value) {
        this.question = question;
        this.answer = answer;
        this.value = value;
    }

    protected String getQuestion() {
        return question;
    }

    protected String getAnswer() {
        return answer;
    }

    protected int getValue() {
        return value;
    }

    abstract protected boolean validateAnswer(String answer);
}


class TrueFalseTriviaQuestion extends TriviaQuestion {
    public TrueFalseTriviaQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    public boolean validateAnswer(String answer) {
        if (super.getAnswer().charAt(0) == answer.charAt(0)) {
            System.out.println("That is correct!  You get " + super.getValue() + " points.");
            return true;
        } else {
            System.out.println("Wrong, the correct answer is " + super.getAnswer());
            return false;
        }
    }

    @Override
    public String toString() {
        return super.getQuestion() +
                "\n" +
                "Enter 'T' for true or 'F' for false.";
    }
}


class FreeFormTriviaQuestion extends TriviaQuestion {
    public FreeFormTriviaQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    public boolean validateAnswer(String answer) {
        if (super.getAnswer().toLowerCase().equals(answer.toLowerCase())) {
            System.out.println("That is correct!  You get " + super.getValue() + " points.");
            return true;
        } else {
            System.out.println("Wrong, the correct answer is " + super.getAnswer());
            return false;
        }
    }

    @Override
    public String toString() {
        return super.getQuestion();
    }
}

public class TriviaGame {

    public List<TriviaQuestion> questions;    // Questions

    public TriviaGame() {
        // Load questions
        questions = new ArrayList<>();
        questions.add(new FreeFormTriviaQuestion(
                "The possession of more than two sets of chromosomes is termed?",
                "polyploidy",
                3));
        questions.add(new TrueFalseTriviaQuestion(
                "Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F",
                1));
        questions.add(new FreeFormTriviaQuestion(
                "1997 British band that produced 'Tub Thumper'",
                "Chumbawumba",
                2));
        questions.add(new FreeFormTriviaQuestion(
                "I am the geometric figure most like a lost parrot",
                "polygon",
                2));
        questions.add(new TrueFalseTriviaQuestion(
                "Generics were introducted to Java starting at version 5.0.",
                "T",
                1));
    }

    private void showQuestion(int index) {
        TriviaQuestion question = questions.get(index);
        System.out.println("Question " + (index + 1) + ".  " + question.getValue() + " points.");
        System.out.println(question.toString());
    }

    // Main game loop

    public static void main(String[] args) {
        int score = 0;            // Overall score
        TriviaGame game = new TriviaGame();
        Scanner keyboard = new Scanner(System.in);
        // Ask a question as long as we haven't asked them all
        for (int i = 0; i < game.questions.size(); i++) {
            // Show question
            game.showQuestion(i);
            // Get answer
            String answer = keyboard.nextLine();
            // Validate answer
            TriviaQuestion q = game.questions.get(i);
            if (q.validateAnswer(answer)) {
                score += q.getValue();
            }
            System.out.println("Your score is " + score);
        }
        System.out.println("Game over!  Thanks for playing!");
    }
}
