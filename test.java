class A {
    static boolean a ;
    static int b ;
    public static boolean m1 ( int input ) {
        boolean ret ;
        if ( b == input ) 
            ret = true ;
        else
            ret = false ;
        return ret ;
    }
}
class B {
    static int b ;
}
public class Main {
    public static void main ( ) {
        boolean d ; 
        d = A . m1 ( B . b ) 
    }
}
EOF