package dbService.dao;

import dbService.dataSets.MessageDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class MessageDAO {

    private Session session;

    public MessageDAO(Session session) {
        this.session = session;
    }

    public MessageDataSet get(long id) throws HibernateException {
        return (MessageDataSet) session.get(MessageDataSet.class, id);
    }

    public List<String> getUserIds() throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class)
                .setProjection(Projections.distinct(Projections.property("userId")));
       return criteria.list();
    }

    public String getUserName(String userId) throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class);
        criteria.add(Restrictions.eq("userId",userId));
        List<MessageDataSet> allMessages = criteria.list();
        return  allMessages.get(0).getUserName();
    }


    public Integer countUserMessages(String userId) throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class);
        return criteria.add(Restrictions.eq("userId",userId)).list().size();
    }

    public Integer countCurseUserMessages(String userId) throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class);
        return criteria
                .add(Restrictions.eq("userId",userId))
                .add(Restrictions.eq("isCurse",true))
                .list().size();
    }

    public List<MessageDataSet> getUserMessages(String userId) throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class);
        criteria.add(Restrictions.eq("userId",userId));
        return criteria.list();
    }

    public List<MessageDataSet> getCurseUserMessages(String userId) throws HibernateException {
        Criteria criteria = session.createCriteria(MessageDataSet.class);
        criteria.add(Restrictions.eq("userId",userId));
        criteria.add(Restrictions.eq("isCurse",true));
        return criteria.list();
    }

    public long insertMessage(Date date, String text, String userId, String chatId, String userName,Boolean hasCurse) throws HibernateException {
        return (Long) session.save(new MessageDataSet(date,text,userId,chatId,userName,hasCurse));
    }
}
