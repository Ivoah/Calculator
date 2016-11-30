import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame implements ActionListener {

  final String[] layout = {
    "(",   ")", "%",   "/",
    "7",   "8", "9",   "*",
    "4",   "5", "6",   "-",
    "1",   "2", "3",   "+",
    "Cls", "0", ".", "=",
  };

  Scanner inFile = null;
  JPanel keypad = null;
  JButton[] keys = null;
  JTextField output = null;

  public Calculator() {

    Font fnt = new Font("Courier", Font.PLAIN, 36);

    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      System.out.println("Error setting look and feel");
    }

    keypad = new JPanel(new GridLayout(5, 4));
    keys = new JButton[5*4];

    for (int i = 0; i < 5*4; i++) {
      keys[i] = new JButton(layout[i]);
      keys[i].addActionListener(this);
      keys[i].setFont(fnt.deriveFont(18f));
      keypad.add(keys[i]);
    }

    output = new JTextField();
    output.setFont(fnt);
    output.setHorizontalAlignment(JTextField.RIGHT);
    output.setEditable(false);

    add(output, BorderLayout.NORTH);
    add(keypad);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setTitle("Calculator");
    setVisible(true);

  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Cls")) {
      output.setText("");
    } else if (e.getActionCommand().equals("=")) {
      try {
        output.setText(Double.toString(eval(output.getText())));
      } catch (EmptyStackException err) {
        output.setText("Error");
      }
    } else {
      output.setText(output.getText() + e.getActionCommand());
    }
  }

  public double eval(String expr) throws EmptyStackException {
    Stack<Character> opStack = new Stack<Character>();
    Stack<Double> valStack = new Stack<Double>();
    for (int i = 0; i < expr.length(); i++) {
      char c = expr.charAt(i);
      if (c >= '0' && c <= '9' || c == '.') {
        double num = c - '0';
        i++;
        int dp = 0;
        if (c == '.') {
          num = 0;
          dp = 1;
        }
        while ( i < expr.length() && ((expr.charAt(i) >= '0' && expr.charAt(i) <= '9') || expr.charAt(i) == '.')) {
          if (expr.charAt(i) == '.') {
            dp = 1;
          } else {
            if (dp > 0) {
              num += (expr.charAt(i) - '0')/Math.pow(10, dp);
              dp++;
            } else {
              num = num*10 + expr.charAt(i) - '0';
            }
          }
          i++;
        }
        i--;
        valStack.push(num);
      } else if (c == '(') {
        opStack.push(c);
      } else if (c == ')') {
        while (opStack.peek() != '(') {
          apply(valStack, opStack);
        }
        opStack.pop();
      } else if (c == ' ') {
        // Ignore it!
      } else {
        while (!opStack.empty() && (prec(c) <= prec(opStack.peek()))) {
          apply(valStack, opStack);
        }
        opStack.push(c);
      }
    }
    while (!opStack.empty()) {
      apply(valStack, opStack);
    }
    return valStack.pop();
  }

  public void apply(Stack<Double> valStack, Stack<Character> opStack) throws EmptyStackException {
    double v2 = valStack.pop();
    double v1 = valStack.pop();
    char op = opStack.pop();
    double ans = 0;
    switch (op) {
      case '*':
        ans = v1 * v2;
        break;
      case '/':
        ans = v1 / v2;
        break;
      case '%':
        ans = v1 % v2;
        break;
      case '+':
        ans = v1 + v2;
        break;
      case '-':
        ans = v1 - v2;
        break;
    }
    valStack.push(ans);
  }

  public int prec(char op) {
    switch (op) {
      case '*':
      case '/':
      case '%':
        return 2;
      case '+':
      case '-':
        return 1;
      default:
        return 0;
    }
  }

  public static void main(String[] args) throws EmptyStackException {
    Calculator calc = new Calculator();
  }

}
