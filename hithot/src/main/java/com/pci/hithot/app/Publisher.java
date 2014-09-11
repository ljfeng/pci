package com.pci.hithot.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.pci.hithot.entity.BianhaoClickCount;
import com.pci.hithot.entity.CandidateTopic;
import com.pci.hithot.entity.ClickRecord;
import com.pci.hithot.entity.ClickRecords;
import com.pci.hithot.entity.TopicProposal;
import com.pci.hithot.entity.UserClickCount;
import com.pci.hithot.entity.UserPreference;

/**
 * 
 * This is application class, stand for spit every users preferred topics
 * 
 * @author jianliu
 * 
 */
public class Publisher {

	private String hisPath;
	private String candiPath;
	private Collection<ClickRecord> recordList;
	private int day = 0;
	private Map<String, UserPreference> userPrefMap;
	private Map<String, UserClickCount> userMap;
	private Map<String, BianhaoClickCount> bianhaoMap;
	private Map<String, List<TopicProposal>> topicProposal;


	private int candiCount = 10;
	private String targetPath;

	public Publisher(String hisPath, String candiPath, String targetPath) {
		this.hisPath = hisPath;
		this.candiPath = candiPath;
		this.targetPath = targetPath;
		init();
	}

	private void init() {
		recordList = new ArrayList<ClickRecord>();
		userPrefMap = new HashMap<String, UserPreference>();
		userMap = new HashMap<String, UserClickCount>();
		bianhaoMap = new HashMap<String, BianhaoClickCount>();
		topicProposal = new HashMap<String, List<TopicProposal>>();
	}

	/**
	 * @param args
	 * 
	 *            args[0]: history excel path args[1]: candidate topic excel
	 *            path
	 */
	public static void main(String[] args) {
		String hisPath = args[0];
		String candiPath = args[1];
		String targetPath = args[2];
		Publisher publisher = new Publisher(hisPath, candiPath, targetPath);
		publisher.setupKnowledgeBase();
		publisher.buildTopicProposals();
		publisher.save();
	}

	private void save() {
		File nfile = new File(targetPath);

		try {
			if (nfile.exists()) {
				File backFile = new File(nfile.getParent(), nfile.getName()
						+ "." + System.currentTimeMillis());
				backFile.createNewFile();
				IOUtils.copy(new FileInputStream(nfile), new FileOutputStream(
						backFile));
			} else {
				nfile.createNewFile();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!nfile.exists()) {
			try {
				nfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		Xcelite xcelite = new Xcelite();
		// Save Record
		XceliteSheet sheet = xcelite.createSheet("records");
		SheetWriter<ClickRecord> writer = sheet
				.getBeanWriter(ClickRecord.class);
		writer.write(recordList);
		// Save user's pref bianhao
		sheet = xcelite.createSheet("user_bianhao_count");
		SheetWriter<UserPreference> preferWriter = sheet
				.getBeanWriter(UserPreference.class);
		Iterator<Entry<String, UserPreference>> upIt = userPrefMap.entrySet()
				.iterator();
		Collection<UserPreference> upList = new ArrayList<UserPreference>();
		while (upIt.hasNext()) {
			upList.add(upIt.next().getValue());
		}
		preferWriter.write(upList);
		// Hot user
		sheet = xcelite.createSheet("users_click_count");
		SheetWriter<UserClickCount> ucWriter = sheet
				.getBeanWriter(UserClickCount.class);
		Iterator<Entry<String, UserClickCount>> uccIt = userMap.entrySet()
				.iterator();
		Collection<UserClickCount> uccList = new ArrayList<UserClickCount>();
		while (uccIt.hasNext()) {
			uccList.add(uccIt.next().getValue());
		}
		ucWriter.write(uccList);
		// Hot fenlei/Bianhao
		sheet = xcelite.createSheet("bianhao_click_count");
		SheetWriter<BianhaoClickCount> bccWriter = sheet
				.getBeanWriter(BianhaoClickCount.class);
		Iterator<Entry<String, BianhaoClickCount>> bccIt = bianhaoMap
				.entrySet().iterator();
		Collection<BianhaoClickCount> bccList = new ArrayList<BianhaoClickCount>();
		while (bccIt.hasNext()) {
			bccList.add(bccIt.next().getValue());
		}
		bccWriter.write(bccList);
		// Save Proposals
		sheet = xcelite.createSheet("proposals");
		SheetWriter<TopicProposal> pWriter = sheet
				.getBeanWriter(TopicProposal.class);
		Iterator<List<TopicProposal>> pit = topicProposal
				.values().iterator();
		while (pit.hasNext()) {
			pWriter.write(pit.next());
		}
		//Save to file
		xcelite.write(nfile);
	}

	private void buildTopicProposals() {
		// Read candidate topics
		Xcelite xcelite = new Xcelite(new File(this.candiPath));
		XceliteSheet sheet = xcelite.getSheet("topics");
		SheetReader<CandidateTopic> reader = sheet
				.getBeanReader(CandidateTopic.class);
		reader.skipHeaderRow(true);
		Collection<CandidateTopic> topics = reader.read();
		if (topics.isEmpty()) {
			System.out.println("There is no candidate topics.");
			return;
		}

		// Calculate proposals for every user
		topicProposal = new HashMap<String, List<TopicProposal>>();
		Iterator<String> it = userMap.keySet().iterator();
		while (it.hasNext()) {
			String youxiang = it.next();
			List<TopicProposal> proposals = calculateProposal(youxiang, topics, userPrefMap);
			topicProposal.put(youxiang, proposals);
		}

	}

	private List<TopicProposal> calculateProposal(String youxiang,
			Collection<CandidateTopic> candidateTopics, Map<String, UserPreference> prefMap) {
		List<TopicProposal> sortList = new ArrayList<TopicProposal>();
		Map<String, Integer> bianhaoHot = getBianhaoHotForUser(youxiang, prefMap);
		Iterator<CandidateTopic> it = candidateTopics.iterator();
		while(it.hasNext()) {
			CandidateTopic topic = it.next();
			if (StringUtils.isEmpty(topic.getWenzhangming())) {
				continue;
			}
			int score = calculateScore(bianhaoHot, topic);
			TopicProposal proposal = new TopicProposal();
			proposal.setScore(score);
			proposal.setWenzhangId(topic.getWenzhangId());
			proposal.setWenzhangming(topic.getWenzhangming());
			proposal.setYouxiang(youxiang);
			sortList.add(proposal);
		}
		//Sort
		Collections.sort(sortList);
		//Return top
		if (sortList.size() > candiCount) {
			List<TopicProposal> list = new ArrayList<TopicProposal>(candiCount);
			for (int i = 0; i < candiCount; i++) {
				list.add(sortList.get(i));
			}
			return list;
		} else {
			return sortList;
		}
		

	}

	/**
	 * TODO Need more refine here, now only count the hit hot
	 * 
	 * @param bianhaoHot
	 * @param topic
	 * @return
	 */
	private int calculateScore(Map<String, Integer> bianhaoHot,
			CandidateTopic topic) {
		String bianhao = topic.getBianhao();
		if (bianhaoHot.containsKey(bianhao)) {
			if (bianhaoHot.get(bianhao).intValue() > 1) {
				System.out.println(bianhao);
			}
			return bianhaoHot.get(bianhao).intValue();
		} else {
			return 0;
		}
	}

	private Map<String, Integer> getBianhaoHotForUser(String youxiang,
			Map<String, UserPreference> prefMap) {
		 Map<String, Integer> result = new HashMap<String, Integer>();
		Iterator<UserPreference> it = prefMap.values().iterator();
		while(it.hasNext()) {
			UserPreference up = it.next();
			if (youxiang.equals(up.getYouxiang())) {
				result.put(up.getBianhao(), up.getCount());
			}
		}
		return result;
	}

	private void setupKnowledgeBase() {
		Xcelite xcelite = new Xcelite(new File(hisPath));
		XceliteSheet sheet = xcelite.getSheet("records");
		SheetReader<ClickRecords> reader = sheet
				.getBeanReader(ClickRecords.class);
		reader.skipHeaderRow(true);
		Collection<ClickRecords> recordss = reader.read();
		Iterator<ClickRecords> it = recordss.iterator();
		while (it.hasNext()) {
			ClickRecords row = it.next();
			String youxiangs = row.getYouxiang();
			if (StringUtils.isEmpty(youxiangs)) {
				continue;
			}
			String[] yarr = splitYouXiang(youxiangs);
			BianhaoClickCount bcc = bianhaoMap.get(row.getBianhao());
			if (bcc == null) {
				bcc = new BianhaoClickCount();
				bcc.setBianhao(row.getBianhao());
				bianhaoMap.put(row.getBianhao(), bcc);
			}
			bcc.setCount(bcc.getCount() + yarr.length);

			for (int i = 0; i < yarr.length; i++) {
				if (StringUtils.isEmpty(yarr[i])) {
					continue;
				}
				if (yarr[i].indexOf("@") != yarr[i].lastIndexOf("@")) {
					System.out.println("Invalid email : " + yarr[i]);
				}
				ClickRecord record = new ClickRecord();
				record.setBianhao(row.getBianhao());
				record.setWenzhangming(row.getWenzhangming());
				record.setYouxiang(yarr[i]);
				recordList.add(record);
				// hot cal
				String hid = record.getYouxiang() + record.getBianhao() + day;
				UserPreference pref = userPrefMap.get(hid);
				if (pref == null) {
					pref = new UserPreference();
					pref.setBianhao(record.getBianhao());
					pref.setYouxiang(record.getYouxiang());
					userPrefMap.put(hid, pref);
				}
				pref.setCount(pref.getCount() + 1);

				// user click cal
				String uid = record.getYouxiang();
				UserClickCount user = userMap.get(uid);
				if (user == null) {
					user = new UserClickCount();
					user.setYouxiang(uid);
					userMap.put(uid, user);
				}
				user.setCount(user.getCount() + 1);
			}
		}
		System.out.println("Get records count: " + recordss.size());
		System.out.println("Get record count: " + recordList.size());
		System.out.println("Get pref count: " + userPrefMap.size());
		System.out.println("Get user count: " + userMap.size());
		System.out.println("Get bianhao count: " + bianhaoMap.size());

	}

	private String[] splitYouXiang(String youxiangs) {
		String temp = youxiangs.replace("\r", ",");
		temp = temp.replace("\r\n", ",");
		temp = temp.replace("\r\n", ",");
		temp = temp.replace("\t", ",");
		temp = temp.replace("£¬", ",");
		temp = temp.replace(" ", ",");
		return temp.split(",");
	}

	/**
	 * API failed to read excel, so create a template excel, and copy data in.
	 */
	public static void doPrepare(String path, Object entity) {
		File nfile = new File(path);
		if (!nfile.exists()) {
			try {
				nfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		Xcelite xcelite = new Xcelite();
		XceliteSheet sheet = xcelite.createSheet("records");
		SheetWriter writer = sheet.getBeanWriter(entity.getClass());
		List<Object> objs = new ArrayList<Object>();
		objs.add(entity);
		// ...fill up users
		writer.write(objs);
		xcelite.write(nfile);
	}

}
