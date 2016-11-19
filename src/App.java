import org.apache.activemq.ActiveMQConnectionFactory;
 
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
 

public class App {
 
    public static void main(String[] args) throws Exception {
        
        
            thread(new NumericProducer(), false);
            Thread.sleep(300);
            thread(new SletterProducer(), false);
            Thread.sleep(300);
            thread(new CletterProducer(), false);
            
            thread(new Consumer(), false); 
        
        
    }
 
    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
        /*
        try{
        brokerThread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        */
    }
 
 
    public static class NumericProducer implements Runnable {
        private volatile boolean cancelled;
        public void run() {
            while (!cancelled) { 
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
                
                for(int i=0; i<=9; i++){
                
                
                String text = i +" : "+ Thread.currentThread().getName() ;
                TextMessage message = session.createTextMessage(text);
 
                // Tell the producer to send the message
                //System.out.println("Sent message: "+i+ " : " + Thread.currentThread().getName());
                producer.send(message);
                }
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
        }
        public void cancel()
            {
               cancelled = true;  
            }

        public boolean isCancelled() {
           return cancelled;
        }
         }
 
    public static class SletterProducer implements Runnable {
        private volatile boolean cancelled;
        public void run() {
            while (!cancelled) {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
                
                for(char ch = 'a'; ch <= 'z'; ch++){
                
                
                String text = ch +" : "+ Thread.currentThread().getName() ;
                TextMessage message = session.createTextMessage(text);
 
                // Tell the producer to send the message
                //System.out.println("Sent message: "+ch+ " : " + Thread.currentThread().getName());
                producer.send(message);
                }
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
            }
        }
        public void cancel()
            {
               cancelled = true;  
            }

        public boolean isCancelled() {
           return cancelled;
        }
    }
 
    public static class CletterProducer implements Runnable {
        private volatile boolean cancelled;
        public void run() {
            while (!cancelled) {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
                
                for(char ch = 'A'; ch <= 'Z'; ch++){
                
                
                String text = ch +" : "+ Thread.currentThread().getName() ;
                TextMessage message = session.createTextMessage(text);
 
                // Tell the producer to send the message
                //System.out.println("Sent message: "+ch+ " : " + Thread.currentThread().getName());
                producer.send(message);
                }
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
            }
        }
        public void cancel()
            {
                cancelled = true;  
            }

        public boolean isCancelled() {
            return cancelled;
        }
    }
 
    public static class Consumer implements Runnable, ExceptionListener {
        private volatile boolean cancelled;
        public void run() {
            while (!cancelled) {
            try {
 
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                connection.setExceptionListener(this);
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);
 
                // Wait for a message
                Message message = consumer.receive(1000);
 
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }
 
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
            }
        }
        public void cancel()
            {
               cancelled = true;  
            }

        public boolean isCancelled() {
           return cancelled;
        }
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }



}