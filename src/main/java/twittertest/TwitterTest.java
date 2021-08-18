package twittertest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;


public class TwitterTest {
	public static void main(String[] args) throws TwitterException{
	    // このファクトリインスタンスは再利用可能でスレッドセーフです

        String[] searchWords = {"メガビタミン"};
        getOneWayLove();
//        getFollowers();
//        showSearch(searchWords);
		// showHomeTimeline();
	}

	private static void getOneWayLove() throws TwitterException{
		try {
			//#### 【事前準備】各種Twitterインスタンスの生成
			//# Twitterクラスのインスタンス生成
			Twitter twitter = new TwitterFactory().getInstance();
			//#### ①ユーザ情報の取得
			//# (フォロワーの一覧を出したい対象のユーザ情報)
			User user = twitter.showUser("_taro_taro_0851");
			int counter=1;
			System.out.println("#### 処理開始 ####");
			//# ②ユーザ情報が取得できた場合
			if (user.getStatus() != null) {
				//# ③フォロワーの一覧を取得
				//# 検索対象のユーザのフォロワーのID一覧を取得
				IDs followerIDs = twitter.getFollowersIDs(user.getScreenName(), -1);
				long[] foids = followerIDs.getIDs();
				IDs friendIDs = twitter.getFriendsIDs(user.getScreenName(), -1);
				long[] frids = friendIDs.getIDs();
				
				System.out.println("Follower IDの数" + foids.length);
				System.out.println("Friend   IDの数" + frids.length);

				ArrayList<Long> mutualids = new ArrayList<>();
				ArrayList<Long> onewayfrids = new ArrayList<>();
				for(long frid : frids) {
					//# 両想いチェックフラグ
					boolean mutual = false;
					boolean onewayfr = true;
					for(long foid : foids) {
						//# 両想いチェック
						if(frid == foid) {
							mutual = true;
							onewayfr = false;
							mutualids.add(frid);
						}
					}
					if(onewayfr == true) {
						onewayfrids.add(frid);
					}
//					if(mutual == true) System.out.print("****");
//					System.out.print(mutual + "\n");

				}
				
				for(int i =0; i < mutualids.size(); i++) {
					//# フォロワーの情報を取得
					User follower = twitter.showUser(mutualids.get(i));
					//# フォロワーの情報を表示
					System.out.printf("No.%-6s ID:%-30s ScreenID：@%-15s フォロワー数：%6s 名前：%-40s\r\n",i,follower.getId(),follower.getScreenName(),follower.getFollowersCount(),follower.getName());
//					System.out.println("両想い ID = " + mutualids.get(i));
				}
				
				for(int i =0; i < onewayfrids.size(); i++) {
					//# フォロワーの情報を取得
					User follower = twitter.showUser(onewayfrids.get(i));
					//# フォロワーの情報を表示
				System.out.printf("No.%-6s ID:%-30s ScreenID：@%-15s フォロワー数：%6s 名前：%-40s\r\n",i,follower.getId(),follower.getScreenName(),follower.getFollowersCount(),follower.getName());
				}
				
				
			} else {
				//# ④ユーザ名のみ出力
				//# →指定したユーザは非公開のため、フォロワー取得不可（ユーザ名のみ表示）
				System.out.println("====["+user.getScreenName()+"]は非公開のため、フォロワーは取得できません");
			}
	        System.out.println("#### 処理完了 ####");
	        System.exit(0);

		} catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("#### フォロワーの一覧出力に失敗しました: " + te.getMessage());
	        System.exit(-1);
		}
	}
	
    private static void getFollowers() throws TwitterException{
    	try {
        //#### 【事前準備】各種Twitterインスタンスの生成
        //# Twitterクラスのインスタンス生成
        Twitter twitter = new TwitterFactory().getInstance();
        //#### ①ユーザ情報の取得
        //# (フォロワーの一覧を出したい対象のユーザ情報)
        User user = twitter.showUser("7231Nony");
        int counter=1;
        
        System.out.println("#### 処理開始 ####");
        //# ②ユーザ情報が取得できた場合
        if (user.getStatus() != null) {
   
          //# ③フォロワーの一覧を取得
          //# 検索対象のユーザのフォロワーのID一覧を取得
          IDs followerIDs = twitter.getFollowersIDs(user.getScreenName(), -1);
//          IDs followerIDs = twitter.getFriendsIDs(user.getScreenName(), -1);
          long[] ids = followerIDs.getIDs();
          System.out.println(Arrays.toString(ids));

          //# 各フォロワーを１人ずつループ
          for(long id : ids) {
              //# フォロワーの情報を取得
              User follower = twitter.showUser(id);
              //# フォロワーの情報を表示
              System.out.printf("No.%-6s ID:%-30s ScreenID：@%-15s フォロワー数：%6s 名前：%-40s\r\n",counter,follower.getId(),follower.getScreenName(),follower.getFollowersCount(),follower.getName());
              counter++;
          }
        } else {
          //# ④ユーザ名のみ出力
          //# →指定したユーザは非公開のため、フォロワー取得不可（ユーザ名のみ表示）
          System.out.println("====["+user.getScreenName()+"]は非公開のため、フォロワーは取得できません");
        }
        System.out.println("#### 処理完了 ####");
        System.exit(0);
      } catch (TwitterException te) {
        te.printStackTrace();
        System.out.println("#### フォロワーの一覧出力に失敗しました: " + te.getMessage());
        System.exit(-1);
      }
    }
	
	public static void showSearch(String[] searchWords) {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query();
        query.setQuery(String.join(" ", searchWords));
        query.setCount(10);
//        query.setSince("2021-07-01");
//        query.setUntil("2021-08-13");

        // 検索実行
        try {
        	System.out.println(">> Search");
        	QueryResult result = twitter.search(query);
        	System.out.println("ヒット数 : " + result.getTweets().size());
            // 検索結果
            for (Status tweet : result.getTweets()) {
            	System.out.println(tweet.getText());
                System.out.println(tweet.getText().replace("\n", ""));
                System.out.println("--");
            }

        } catch (TwitterException e) {
        	System.out.println("TwitterException Error");
        }
	}
	
	
	public static void showHomeTimeline() {
		System.out.println("■factory.");
		Twitter twitter = new TwitterFactory().getInstance();	//認証なしで接続
		
		
		System.out.println("■Showing public timeline.");
		try {
			List<Status> statuses = twitter.getHomeTimeline(); //パブリックタイムラインの取得
			for (Status status : statuses) {
				System.out.println(status.getUser().getName() + ":" + status.getUser().getTimeZone()
						+ " : " + status.getText());
			}
		} catch (TwitterException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
