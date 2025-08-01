package com.medapp;

public class Index {
    public static void main(String[] args) {
        int port = 3000;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }
        
        System.out.println("🚀 Server is running at http://localhost:" + port);
    }
}
