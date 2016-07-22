package com.hogwart.simpledb;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SimpleDb db = new SimpleDb();

        while(true) {
            String[] commands = sc.nextLine().split(" ");

            Commands command = Commands.valueOf(commands[0]); //TODO invalid command
            if (command == Commands.END) {
                break;
            }

            String field = commands.length > 1 ? commands[1] : null;
            Object value = commands.length > 2 ? commands[2] : null;

            db.execute(command, field, value);
        }
    }
}
