package com.santhosh.urlshortener.util;

public class Base62 {
    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int BASE = ALPHABET.length();
    private Base62() {
        // Utility class: prevent object creation
    }
    public static String encode(long id){
        if(id<0){
            throw new IllegalArgumentException("ID must be non-negative");
        }
        if(id==0){
            return String.valueOf(ALPHABET.charAt(0));
        }
        StringBuilder result= new StringBuilder();
        while(id>0){
            int remainder= (int) (id%BASE);
            result.append(ALPHABET.charAt(remainder));
            id/=BASE;
        }
        return result.reverse().toString();


    }
    public static long decode(String code){
        if(code==null || code.isEmpty()){
            throw new IllegalArgumentException("Code must not be null or empty");
        }
        long id=0;
        for(int i=0;i<code.length();i++){
            char c= code.charAt(i);
            int index= ALPHABET.indexOf(c);
            if(index==-1){
                throw new IllegalArgumentException("Invalid character in code: "+c);
            }
            id=Math.addExact(Math.multiplyExact(id,BASE),index);
        }
        return id;

    }
}
