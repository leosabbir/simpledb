package com.hogwart.simpledb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = null;// = new Scanner(System.in);

        if (args.length > 0) {
            try {
                sc = new Scanner(new FileInputStream(new File(args[0])));
            } catch (FileNotFoundException e) {
                System.err.println("Specified file was not found");
                return;
            }

        } else {
            sc = new Scanner(System.in);
        }

        SimpleDb db = SimpleDb.GET_INSTANCE();

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
