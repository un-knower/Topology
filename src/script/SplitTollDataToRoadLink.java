package script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * 拆分收费数据为路链
 * @author zhangjingtao
 *
 */
public class SplitTollDataToRoadLink {
	public static void main(String[] args){
		HashMap<String, Integer> roadlink_count_Map=new HashMap<String, Integer>();
		HashMap<String, String[]> station_poi_Map=new HashMap<String, String[]>();
		HashMap<String, String> poi_shortestPath_Map=new HashMap<String, String>();
		HashMap<String, Double> poi_SPcost_Map=new HashMap<String,Double>();
		String tollDataPath ="G:\\zjt\\2016年10月13日拆分收费信息实验用\\0419.csv";
		String staionPOIPath ="G:\\zjt\\2016年10月13日拆分收费信息实验用\\stationPOI.csv";
		String spPath ="G:\\zjt\\2016年10月13日拆分收费信息实验用\\shandong.csv";
		String outputPath ="G:\\zjt\\2016年10月13日拆分收费信息实验用\\output\\result.csv";
		BufferedReader tollDataReader=null,staionPOIReader=null,spReader=null;
		BufferedWriter writer=null;
		try {
			tollDataReader=new BufferedReader(new InputStreamReader(new FileInputStream(tollDataPath), "UTF-8"));
			staionPOIReader=new BufferedReader(new InputStreamReader(new FileInputStream(staionPOIPath), "UTF-8"));
			spReader=new BufferedReader(new InputStreamReader(new FileInputStream(spPath), "UTF-8"));
			writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath),"UTF-8"));
			String line,string;
			while((line=staionPOIReader.readLine())!=null){
				String[] array=line.split(",");
				station_poi_Map.put(array[0], array[2].split("\\|"));
			}
			while((line=spReader.readLine())!=null){
				String[] array=line.split(",");
				String key=array[0]+","+array[1];
				if(array.length==4)
					poi_shortestPath_Map.put(key, array[3]);
				try {
					poi_SPcost_Map.put(key, Double.valueOf(array[2]));
				} catch (Exception e) {
					System.out.println(line);
					e.printStackTrace();
				}
				
			}
			while((line=tollDataReader.readLine())!=null){
				String[] array=line.split(",");
				String key=getSPkey(array[5], array[7], station_poi_Map, poi_SPcost_Map);
				if(key==null)
					continue;
				if(!poi_shortestPath_Map.containsKey(key))
					continue;
				String[] sp=poi_shortestPath_Map.get(key).split("\\|");
				for(int index=0;index<sp.length-1;index++){
					String key2=sp[index]+","+sp[index+1];
					if(roadlink_count_Map.containsKey(key2)){
						int value=roadlink_count_Map.get(key2);
						roadlink_count_Map.put(key2, value+1);
					}
					else{
						roadlink_count_Map.put(key2,1);
					}
				}
			}
			for(String key : roadlink_count_Map.keySet()){
				writer.write(key+","+roadlink_count_Map.get(key)+"\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				writer.close();
				tollDataReader.close();
				staionPOIReader.close();
				spReader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static String getSPkey(String origin,String destination,HashMap<String, String[]> station_poi_Map,HashMap<String, Double> poi_SPcost_Map){
		String[] origin_array=station_poi_Map.get(origin);
		String[] destination_array=station_poi_Map.get(destination);
		String key=null;
		double min=Integer.MAX_VALUE;
		if(origin_array==null||destination_array==null)
			return null;
		for(String origin_poi : origin_array){
			for(String destination_poi : destination_array){
				String temp=origin_poi+","+destination_poi;
				if(!poi_SPcost_Map.containsKey(temp))
					continue;
				double cost=poi_SPcost_Map.get(temp);
				if(cost<min)
					key=temp;
			}
		}
		return key;
	}
}
