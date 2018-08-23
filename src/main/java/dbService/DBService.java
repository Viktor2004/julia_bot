package dbService;

import dbService.dao.MessageDAO;
import dbService.dataSets.MessageDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.telegram.telegrambots.api.objects.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DBService {
    private static final String hibernate_show_sql = "false";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);
    }

    @SuppressWarnings("UnusedDeclaration")
    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(MessageDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(MessageDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    public MessageDataSet getMessage(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            MessageDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<String> getUserIds() throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            List<String> ids = dao.getUserIds();
            session.close();
            return ids;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<MessageDataSet> getUserMessages(String userId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            List<MessageDataSet> id = dao.getUserMessages(userId);
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public String getUserName(String userId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            String userName = dao.getUserName(userId);
            session.close();
            return userName;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<MessageDataSet> getCurseUserMessages(String userId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            List<MessageDataSet> id = dao.getCurseUserMessages(userId);
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Integer countUserMessages(String userId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            Integer id = dao.countUserMessages(userId);
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Integer countCurseUserMessages(String userId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            MessageDAO dao = new MessageDAO(session);
            Integer id = dao.countCurseUserMessages(userId);
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void addMessage(Message message, Boolean hasCurse) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            MessageDAO dao = new MessageDAO(session);
            String userId = message.getFrom().getId() == null ? "" : message.getFrom().getId().toString();
            String chatId = message.getChatId() == null ? "" : message.getChatId().toString();
            String userName =
                    (message.getFrom().getUserName() == null ? "" : message.getFrom().getUserName() + " ")
                            + (message.getFrom().getFirstName() == null ? "" : message.getFrom().getFirstName() + " ")
                            + (message.getFrom().getLastName() == null ? "" : message.getFrom().getLastName());

            long id = dao.insertMessage(
                    new Date(),
                    message.getText(),
                    userId,
                    chatId,
                    userName,
                    hasCurse
            );
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
