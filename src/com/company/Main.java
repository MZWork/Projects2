package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, DummyException {
        FileInputStream file_IN;
        try {
            file_IN = new FileInputStream(args[0]);
        }
        catch(FileNotFoundException e) {
            System.out.println("File Not Found!");
            return;
        }

        Scanner scanIni = new Scanner(file_IN);
        Scanner scannerSys = new Scanner(System.in);
        List<String> messageList = new ArrayList<String>();
        while (scanIni.hasNextLine()) {
            String line = scanIni.nextLine();
            messageList.add(line);
        }
        Iterator<String> iter = messageList.iterator();
        ConnectionImpl connection = new ConnectionImpl();
        SessionImpl session = (SessionImpl) connection.createSession(true);
        DestinationImpl destination = (DestinationImpl) session.createDestination("Test1");
        ProducerImpl producer = new ProducerImpl(destination);

        do {
            for (int i = 0; i < messageList.size(); i++) {
                producer.send(messageList.get(i));
                TimeUnit.SECONDS.sleep(2);
            }
            System.out.println("No more strings found. Type 1 to stop program or anything else to continue.");
        }
        while (!(scannerSys.nextLine().equals("1")));
        session.close();
        connection.close();
    }
}
