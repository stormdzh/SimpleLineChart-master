# An Android simple LineChart 

###Perview
![image](https://github.com/stormdzh/SimpleLineChart-master/blob/master/perview.png)
###How To Use
Add a SimpleLineChart into your XML.

  <com.wingsofts.testlinechart.SimpleLineChart
        android:background="#22ff0000"
        android:id="@+id/simpleLineChart"
        android:layout_width="400dp"
        android:layout_height="200dp" />
		
You must set XAxis item and YAxis item and dataSource like:
 mSimpleLineChart = (SimpleLineChart) findViewById(R.id.simpleLineChart);
        String[] xItem = {"2013","2014","2016","2017"};
        String[] yItem = {"10k","20k","30k","40k","50k"};
        if(mSimpleLineChart == null)
            Log.e("wing","null!!!!");
        mSimpleLineChart.setXItem(xItem);
        mSimpleLineChart.setYItem(yItem);

        HashMap<String,Integer> pointMap = new HashMap();
        pointMap.put("2013",10);
        pointMap.put("2014",2);
        pointMap.put("2016",4);
        pointMap.put("2017",10);
        mSimpleLineChart.setLine1Data(pointMap);

        HashMap<String,Integer> pointMap2 = new HashMap();
        pointMap2.put("2013",4);
        pointMap2.put("2014",9);
        pointMap2.put("2016",6);
        pointMap2.put("2017",3);
        mSimpleLineChart.setLine2Data(pointMap2);