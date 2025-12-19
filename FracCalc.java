// Rithwik Bhimanadhuni
// Period 6
// Fraction Calculator Project

//Import java classes
import java.util.*;

// This program is a fraction calculator that takes user input and then does fraction math
// with arithmetic operators on fractions, mixed fractions, and whole numbers.
// The calculator can find answers to expressions using the operators of +,-,*,/.
// The result of the mathematical expressions will be a reduced mixed number, reduced fraction, or whole number.
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   public static void main(String[] args) {
   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      System.out.print("Enter: ");
      return console.nextLine();   
   }
   
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }
   
   // Lots work for this project is handled in here.
   // Of course, this method will call LOTS of helper methods
   // so that this method can be shorter.
   // This will calculate the expression and RETURN the answer.
   // This will NOT print anything!
   // Input: an expression to be evaluated
   //    Examples: 
   //        1/2 + 1/2
   //        2_1/4 - 0_1/8
   //        1_1/8 * 2
   // Return: the fully reduced mathematical result of the expression
   //    Value is returned as a String. Results using above examples:
   //        1
   //        2 1/8
   //        2 1/4
   public static String processExpression(String input) {
      Scanner parser = new Scanner(input);
      String firstValue = parser.next();
      String operator = parser.next();
      String secondValue = parser.next();
      //Parts for fraction 1
      int whole1 = getWhole(firstValue);
      int numerator1 = getNumerator(firstValue);
      int denominator1 = getDenominator(firstValue);
      //Parts for fraction 2
      int whole2 = getWhole(secondValue);
      int numerator2 = getNumerator(secondValue);
      int denominator2 = getDenominator(secondValue);
      //Improper numerators for both fractions
      int improperNumerator1 = getImproperNumerator(whole1, numerator1, denominator1);
      int improperNumerator2 = getImproperNumerator(whole2, numerator2, denominator2);
      
      //Calls doMath method to do the math for the operands
      String result = doMath(improperNumerator1, denominator1, operator, improperNumerator2, denominator2);
      //return result
      return result;
   }      

   // Helper method: Gets the whole number part from a mixed fraction
   // Parameter String value: One of the operands
   // Returns the number before the underscore from the mixed fraction
   // Returns 0 if just a fractions, returns value back if just whole number
   public static int getWhole(String value){
      //position of underscore in mixed fractions
      int underscore = value.indexOf("_");
      if (underscore != -1) {
         return Integer.parseInt(value.substring(0,underscore));
      }
      //checks if operand is fraction
      int slash = value.indexOf("/");
      //if not mixed-fraction, no whole number
      if (slash != -1){
         return 0;
      }
      //if whole number, gives back value itself
      return Integer.parseInt(value);
   }

   // Helper method: Gets the numerator from a fraction
   // Parameter String value: One of the operands
   // Returns the part before the divison symbol (/) for fractions and mixed fractions
   // Returns 0 for whole numbers
   public static int getNumerator (String value){
      //finds position of divison symbol
      int slash = value.indexOf("/");
      if (slash != -1){
         //If no slash, checks if mixed number
         int underscore = value.indexOf("_");
         if(underscore != -1){
            //If mixed fraction, gets numerator after underscore
            return Integer.parseInt(value.substring(underscore +1, slash));
         } else {
            //If just a fraction, gets numerator normally
            return Integer.parseInt(value.substring(0,slash));
         }
      }
      //If whole number, no numerator
      return 0;
   }   

   // Helper method: Gets denominator from a fraction
   // Parameter String value: One of the operands
   // Returns the part after the division symbol (/) for fractions and mixed fractions if it exists
   // Returns 1 if no denominator exists
   public static int getDenominator (String value){
      //finds position of division symbol
      int slash = value.indexOf("/");
      if (slash != -1){
         //Gets value after slash
         return Integer.parseInt(value.substring(slash+1));
      }
      //If whole number, denominator is just 1
      return 1;
   }

   // Helper method: Gets the Greatest Common Divisor (GCD) of numerator and denominator
   // Parameter int a: numerator
   // Paramter int b: denominator
   // Finds the biggest number that can be evenly divided with both the numerator and denominator
   // Allows for a reduced fraction
   // Returns Greatest Common Divisor (GCD)
   public static int getGCD(int a, int b){
      //Sets gcd value intially to 1, smallest gcd
      int gcdValue = 1;
      //checks if i can evenly divide both numbers
      for (int i = 1; i<=Math.min(a,b);i++){
         if(a%i == 0 && b % i == 0){
            //if so, changes gcd value
            gcdValue = i;
         }
      }
      return gcdValue;
   }

   // Takes whole number and numerator from the mixed fraction and turns it into the improper numerator
   // Parameter int whole: whole number part of mixed fraction
   // Paramter int numerator: numerator from fraction
   // Parameter int denominator: denominator from fraction
   // Multiplies whole by denominator and then adds (or substracts if whole number is negative) the numerator
   // Returns value of newly found improper numerator
   public static int getImproperNumerator(int whole, int numerator, int denominator){
      if (whole < 0){
         //for a whole number in a mixed fraction that is negative
         return whole * denominator - numerator;
      } else {
         //for a whole number in a mixed fraction that is positive
         return whole * denominator + numerator;
      }
   }

   // Helper method: Does the math on the two improper fractions with the selected operator
   // Calcuates the result of the numerator and denominaotr
   // Makes sure denominator is always positive, reduces the fraction with the GCD
   // Parameter int num1: numerator of first fraction, int num2: numerator of second fraction
   // Parameter int den1: denominator of first fraction, int den2: denominator of second fraction
   // Parameter String op: operator
   // Returns properly formatted result as a String
   public static String doMath(int num1, int den1, String op, int num2, int den2){
      //Variables for each the result of denominator and numerator
      int numeratorResult = 0;
      int denominatorResult = 0;
      //If operator is +, adds fractions
      if(op.equals("+")){
      numeratorResult = num1 * den2 + num2 * den1;
      denominatorResult = den1*den2;
      }
      //If operator is -, subtracts fractions
      if(op.equals("-")){
         numeratorResult = num1 * den2 - num2 * den1;
         denominatorResult = den1 * den2;
      }
      //If operator is *, multiplies fractions
      if(op.equals("*")){
         numeratorResult = num1 * num2;
         denominatorResult = den1 * den2;
      }
      //If operator is /, divides fractions
      if(op.equals("/")){
         //Multiply with reciprocal of second operand (second fraction)
         numeratorResult = num1 * den2;
         denominatorResult = num2 * den1;
      }
      //If denominator result is zero, return "don't divide by zero"
      if (denominatorResult == 0){
         return "You cannot divide by zero";
      }
      //If denominator result is less than -1, moves negative to the numerator
      if (denominatorResult < 0){
         numeratorResult *=-1;
         denominatorResult *=-1;
      }
      //Reducing numerator and denominator, gcd needs to be positive
      int gcd = getGCD(Math.abs(numeratorResult),Math.abs(denominatorResult));
      numeratorResult /=gcd;
      denominatorResult /=gcd;

      //formatted result returned
      return getFormattedAnswer(numeratorResult,denominatorResult);
   }

   // Helper method: Converts result into correct format for the user
   // Finds out if the result is a fraction, mixed fraction, or whole number
   // Parameter int numerator: numerator of fraction
   // Parameter int denominator: denominator of fraction
   // Returns formatted result as String
   public static String getFormattedAnswer(int numerator, int denominator){
      //calculate whole number
      int whole = numerator/denominator;
      //caculate remainder
      int remainder = numerator % denominator;
      //make remainder positive (for mixed fractions to be proper)
      if (remainder < 0){
         remainder = -1 * remainder;
      }
      //returns whole number, fraction, or mixed number
      if (remainder == 0){
         return whole + "";
      } else if (whole == 0){
         return numerator + "/" + denominator;
      } else {
         return whole + " " + remainder + "/" + denominator;
      }
   }
   
   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {
      String help = "\nFraction Calculator Guide:\n";
      help += "Use an underscore for mixed numbers to seperate the whole number and fraction (ex: 3_1/4)\n";
      help += "For fraction parts, use the \\ to divide the numerator by the denominator\n";
      help += "Just enter the number normally for a whole number\n";
      help += "Make sure to use an operator in your expression (+ - * /)\n";
      help += "***Make sure to always add spaces around the operator being used\n";
      help += "***Dont divide by zero, you will get a message saying not to do so if you try to\n";
      help += "Type 'quit' to exit the calculator\n";
      return help;
   }
}
// Resources:
// I got help to code the getGCD method from this website:
// https://www.geeksforgeeks.org/dsa/program-to-find-gcd-or-hcf-of-two-numbers/