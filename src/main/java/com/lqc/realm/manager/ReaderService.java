package com.lqc.realm.manager;

import com.lqc.realm.exception.GoBack;
import com.lqc.realm.exception.ReEnter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Scanner;

/**
 * Author: Glenn
 * Description:
 * Created: 2022/8/2
 */
@Service
public class ReaderService {

    private final Scanner reader_1 = new Scanner(System.in);
    private final Scanner reader_2 = new Scanner(System.in);

    public String getString() throws GoBack, ReEnter {
        String result = reader_1.next();
        if ("-1".equals(result)) {
            throw new ReEnter();
        }
        if ("-2".equals(result)) {
            throw new GoBack();
        }
        if ("cls".equals(result)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        if ("exit".equals(result)) {
            System.out.println();
            System.out.println("----------- bye -----------");
            System.exit(0);
        }
        return result;
    }

    public String getLine() throws GoBack, ReEnter {
        String result = reader_2.nextLine();
        if ("-1".equals(result)) {
            throw new ReEnter();
        }
        if ("-2".equals(result)) {
            throw new GoBack();
        }
        if ("cls".equals(result)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        if ("exit".equals(result)) {
            System.out.println();
            System.out.println("----------- bye -----------");
            System.exit(0);
        }
        return result;
    }

    public int getInt() throws GoBack, ReEnter {
        String string = this.getString();
        return Integer.parseInt(string);
    }

    public int getIntPlus() throws GoBack, ReEnter {
        String string = this.getLine();
        return Integer.parseInt(string);
    }

}
