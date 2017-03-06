package com.example.xiaohai.myapplication.MyEvent;


        import java.util.HashSet;
        import java.util.Iterator;
        import java.util.Set;

/**
 * �¼�Դ.
 * @author Eric
 */
public class EventSourceObject {
    private String str;
    //����������
    private Set<CusEventListener> listener;
    public EventSourceObject(){
        this.listener = new HashSet<CusEventListener>();
        this.str = "defaultstr";
    }
    //���¼�Դע�������
    public void addCusListener(CusEventListener cel){
        this.listener.add(cel);
    }
    //���¼�����ʱ,֪ͨע���ڸ��¼�Դ�ϵ����м�����������Ӧ�ķ�Ӧ�����ûص�������
    protected void notifies(){
        CusEventListener cel = null;
        Iterator<CusEventListener> iterator = this.listener.iterator();
        while(iterator.hasNext()){
            cel = iterator.next();
            cel.fireCusEvent(new CusEvent(this));
        }
    }
    public String getString() {
        return str;
    }
    //ģ���¼�������������Ա����name��ֵ�����仯ʱ�������¼���
    public void setString(String name) {
       // if(!this.str.equals(name))
        {
            this.str = name;
            notifies();
        }
    }
    public  void  removeListener(CusEventListener cel)
    {
        this.listener.remove(cel);
    }
}
