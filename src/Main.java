import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //System.out.print("> ");
        String text = sc.next();
        //String text = "x = 3; y=5; z=(x*y)+y*6-x/2*(9/(4-x));";
//        String text = "x=10;xz=4;y=5;z=x+(y*2);fmt.Println(x);fmt.Println(z);";
        Lexer lexer = new Lexer(text);
        Interpreter interpreter = new Interpreter(lexer);
        interpreter.interpret();
    }
}
