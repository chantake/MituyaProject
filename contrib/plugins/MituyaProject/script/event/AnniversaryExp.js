var task;
var value = 0;
var msg;

function init() {
	NewSchedule(22,0);
	em.getPlugin().Log("1周年記念イベント（経験値）有効");
}

function NewSchedule(hour, min) {
	var cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("JST")); 
	var year = cal.get(java.util.Calendar.YEAR); //現在の年を取得
	var month = cal.get(java.util.Calendar.MONTH);//現在の月を取得
	var day = cal.get(java.util.Calendar.DATE);
	cal.set(year, month, day, hour, min, 0); //開始日時を指定
	//var nextTime = cal.getTime();
	var nextTime = cal.getTimeInMillis();
	while (nextTime <= java.lang.System.currentTimeMillis()) { 
		nextTime += 1000 * 60 * 60 * 24; //1日
	}
	task = em.scheduleAtTimestamp("start", nextTime);
	em.getPlugin().Log("スケジュール完了 "+hour+":"+min);
}

function cancelSchedule() { 
	task.cancel(true); 
} 

function start() {
	switch (value){
		case 0:
			em.setExp("4");
			em.broadcastEventMessage("ただいまより &e経験値4倍&fイベント を開始します");
			msg = "イベント期間中はモンスターより得られる経験値が4倍になります";
			NewSchedule(24,0);
			value = 1;
			break;
		case 1:
			em.setExp("1.5");
			em.broadcastEventMessage("本日の経験値&44倍&fイベントは終了いたしました");
			msg = "ご参加ありがとうございます";
			NewSchedule(22,0);
			value = 0;
			break;
	}
	em.schedule("message", 1000 * 7);
}

function message() {
	em.broadcastEventMessage(msg);
}
