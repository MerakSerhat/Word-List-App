package com.serhatmerak.wordlist.kumar;

public class Yether {
    public void main(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println("i =" + i);
                System.out.println("j =" + j);
            }
        }
    }

    public void aslinda(){
        //İlk for ilk çalıştığında
        {
            int i = 0;
            {
                int j = 0;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 0
                //j = 0
            }

            {
                int j = 1;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 0
                //j = 1
            }
        }



        //İlk for ikinciye çalıştığında
        {
            int i = 1;

            {
                int j = 0;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 1
                //j = 0
            }

            {
                int j = 1;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 1
                //j = 1
            }

        }
        //İlk for üçüncüye çalıştığında
        {
            int i = 2;

            {
                int j = 0;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 2
                //j = 0
            }

            {
                int j = 1;
                System.out.println("i =" + i);
                System.out.println("j =" + j);
                //i = 2
                //j = 1
            }

        }
    }
}
