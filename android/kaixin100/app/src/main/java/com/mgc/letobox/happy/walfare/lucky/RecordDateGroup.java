package com.mgc.letobox.happy.walfare.lucky;



import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by edz on 2017/7/16.
 */

public class RecordDateGroup {


    public static final int ITEM = 0;

    public static final int SECTION = 1;

    public final int type;
    public final LuckyListReponse lucky;

    public int sectionPosition;
    public int listPosition;

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public LuckyListReponse getDetail() {
        return lucky;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public RecordDateGroup(int type, LuckyListReponse detail) {
        super();
        this.type = type;
        this.lucky = detail;
    }

    public RecordDateGroup(int type, LuckyListReponse detail, int sectionPosition,
                           int listPosition) {
        super();
        this.type = type;
        this.lucky = detail;
        this.sectionPosition = sectionPosition;
        this.listPosition = listPosition;
    }

    @Override
    public String toString() {
        return lucky.getCreate_time();
    }
    /**
     * 通过HashMap键值对的特性，将ArrayList的数据进行分组，返回带有分组Header的ArrayList。
     * @param details 从后台接受到的ArrayList的数据，其中日期格式为：yyyy-MM-dd HH:mm:ss
     * @return list  返回的list是分类后的包含header（yyyy-MM-dd）和item（HH:mm:ss）的ArrayList
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ArrayList<RecordDateGroup> getData(List<LuckyListReponse> details){
        //最后我们要返回带有分组的list,初始化
        ArrayList<RecordDateGroup> list = new ArrayList<RecordDateGroup>();
        //WarnDetail作为key是yyyy-MM-dd格式,List<WarnDetail>是对应的值是HH:mm:ss格式
        //Map<LuckyListReponse, List<LuckyListReponse>> map = new HashMap<LuckyListReponse, List<LuckyListReponse>>();
        Map<LuckyListReponse, List<LuckyListReponse>> map = new TreeMap<LuckyListReponse, List<LuckyListReponse>>(
                new Comparator<LuckyListReponse>() {
                    public int compare(LuckyListReponse obj1, LuckyListReponse obj2) {
                        // 降序排序
                        return obj2.getCreate_time().compareTo(obj1.getCreate_time());
                    }
                });
        //按照warndetail里面的时间进行分类
        LuckyListReponse detail = new LuckyListReponse();
        for (int i = 0; i < details.size(); i++) {
            try {
                String key = DateUtil.exchangeStrMonth(details.get(i).getCreate_time()) ;
                if (detail.getCreate_time() != null && !"".equals(detail.getCreate_time())) {
                    //判断这个Key对象有没有生成,保证是唯一对象.如果第一次没有生成,那么new一个对象,之后同组的其他item都指向这个key
                    boolean b = !key.equals(detail.getCreate_time().toString());
                    if (b) {
                        detail = new LuckyListReponse();
                    }
                }
                detail.setCreate_time(key);
                //把属于当天yyyy-MM-dd的时间HH:mm:ss全部指向这个key
                List<LuckyListReponse> warnDetails = map.get(detail);
                //判断这个key对应的值有没有初始化,若第一次进来,这new一个arryalist对象,之后属于这一天的item都加到这个集合里面
                if (warnDetails == null) {
                    warnDetails = new ArrayList<LuckyListReponse>();
                }

                warnDetails.add(details.get(i));

                map.put(detail, warnDetails);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //用迭代器遍历map添加到list里面
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            LuckyListReponse key = (LuckyListReponse) entry.getKey();
            //我们的key(yyyy-MM-dd)作为标题.类别属于SECTION
            list.add(new RecordDateGroup(SECTION, key));
            List<LuckyListReponse> li = (List<LuckyListReponse>) entry.getValue();
            for (LuckyListReponse warnDetail : li) {
                //对应的值(HH:mm:ss)作为标题下的item,类别属于ITEM
                list.add(new RecordDateGroup(ITEM, warnDetail));
            }
        }
        //把分好类的hashmap添加到list里面便于显示
        return list;
    }

}
