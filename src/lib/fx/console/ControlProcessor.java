///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package lib.fx.console;
//
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.es.cu.data.DB;
//import com.es.cu.data.DBVirtual;
//import com.es.cu.data.sphere.Cell;
//import com.es.cu.data.sphere.Sphere;
//import com.es.cu.gate.nzx.NzxClient;
//import com.es.cu.holder.ChatRoomHolder;
//import com.es.cu.holder.GroupHolder;
//import com.es.cu.holder.LiveTvHolder;
//import com.es.cu.holder.PersonHolder;
//import com.es.cu.holder.PlaceHolder;
//import com.es.cu.holder.ServerNotificationHolder;
//import com.es.cu.holder.VodHolder;
//import com.es.cu.main.Main;
//import com.es.cu.main.SN;
//import com.es.cu.message.HMC;
//import com.es.cu.message.HMI;
//import com.es.cu.message.HMK;
//import com.es.cu.message.HMessage;
//import com.es.cu.message.HMessageBank;
//import com.es.cu.misc.MediaUtil;
//import com.es.cu.misc.StopWordsFilter;
//import com.es.cu.misc.Util;
//import com.es.cu.postman.Queue;
//import com.es.cu.postman.QueueList;
//import com.es.cu.processor.x.XP;
//import com.es.cu.processor.y.YP;
//import com.es.cu.processor.y.YPM;
//import com.es.cu.profile.Profile;
//import com.es.cu.profile.VCallProfile;
//import com.es.cu.profile.VoIPProfile;
//import com.hian.appengine.AppEngine;
//import com.hian.appengine.incache.Cache;
//import com.hian.j.io.log.Log;
//import com.hian.j.io.server.Client;
//import com.hian.message.WsvBuilder;
//import java.util.Set;
//
///**
// *
// * @author febri
// *
// */
//public class ControlProcessor {
//    
//    public static final String TAG = "ControlProcessor";
//    
//    public void onReceive(Client p_client, String p_data) {
//        try {
//            WsvBuilder builder = new WsvBuilder(p_data);
//            if (builder.getToken(0).equals("help")) {
//                onHelp(p_client);
//            }
//            else if (builder.getToken(0).equals("show")) {
//                onShow(p_client, builder);
//            }
//            else if (builder.getToken(0).equals("exec") || builder.getToken(0).equals("x")) {
//                onExec(p_client, builder);
//            }
//            else if (builder.getToken(0).equals("set")) {
//                onSet(p_client, builder);
//            }
//        } catch (Exception | Error e) {
//            System.out.println(Log.getStackTraceString(e));
//        }
//    }
//
//    private void onExec(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.getToken(1).equals("dump-outbox")) {
//            onExecDumpOutbox(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("load-outbox")) {
//            onExecLoadOutbox(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("add-feeder")) {
//            onExecAddFeeder(p_client, p_wsv.getToken(2), p_wsv.getToken(3));
//        }
//        else if (p_wsv.getToken(1).equals("add-npm")) {
//            onExecAddNpm(p_client, p_wsv.getToken(2), p_wsv.getToken(3), p_wsv.getToken(4));
//        }
//        else if (p_wsv.getToken(1).equals("remove-feeder")) {
//            onExecRemoveFeeder(p_client, p_wsv.getToken(2));
//        }
//        else if (p_wsv.getToken(1).equals("start-feeder")) {
//            onExecStartFeeder();
//        }
//        else if (p_wsv.getToken(1).equals("interrupt-feeder")) {
//            onExecInterruptFeeder();
//        }
//        else if (p_wsv.getToken(1).equals("stop-feeder")) {
//            onExecStopFeeder();
//        }
//        else if (p_wsv.getToken(1).equals("start-gateways")) {
//            onExecStartGateways(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("stop-gateways")) {
//            onExecStopGateways(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("yp-add")) {
//            onExecYpAdd(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("yp-remove")) {
//            onExecYpRemove(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("load-properties")) {
//            onExecLoadProperties(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("load-place")) {
//            onExecLoadPlace(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("push-place")) {
//            onExecPushPlace(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("add-vb")) {
//            onExecAddVBPlace(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("remove-vb")) {
//            onExecRemoveVBPlace(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("load-livetv")) {
//            onExecLoadLiveTV(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-livetv")) {
//            onExecPushLiveTV(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("load-sn")) {
//            onExecLoadSN(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("broadcast-sn")) {
//            onExecBroadcastSN(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("load-vod")) {
//            onExecLoadVod(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-vod")) {
//            onExecPushVod(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-vod-all")) {
//            onExecPushAllVod(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-removed-buddy")) {
//            onExecPushRemovedBuddy(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("outbox-reset")) {
//            onResetOutbox(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("load-message-filter")) {
//            onExecLoadMessageFilter(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("remove-alias")) {
//            onExecRemoveAlias(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("suggest-open-tender")) {
//            onExecOpenTenderSuggestion(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("set-user-balance")) {
//            onSetUserBalance(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("wl-matches")) {
//            onTryQueryWishlist(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("add-wl")) {
//            onAddWishlist(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("del-wl")) {
//            onDelWishlist(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("see-wl")) {
//            onSeeWishlist(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-group")) {
//            onPushGroup(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-image")) {
//            onPushImage(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("switch-org-parent") || p_wsv.getToken(1).equals("sop")) {
//            onSwitchOrgParent(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("push-sync")) {
//            onExecPushSync(p_client, p_wsv);
//        }
//        
//        
//    }
//    private void onSwitchOrgParent(Client p_client, WsvBuilder p_wsv) {
//    	String group_id   = p_wsv.getToken(2);
//    	String new_parent = p_wsv.getToken(3);
//    	GroupHolder group 	  = Sphere.Group.get(group_id);
//    	if (group == null) {
//    		p_client.write("not found " + p_wsv.getToken(2));
//    		return;
//    	}
//    	if (Sphere.Group.get(new_parent) == null) {
//    		p_client.write("not found " + p_wsv.getToken(3));
//    		return;
//    	}
//    	int i = DB.update("UPDATE GROUPS SET PARENT_ID = '" + new_parent + "' WHERE GROUP_ID = '" + group_id + "'", true);
//    	if (i > 0) {
//    		Sphere.Group.switchParent(group, new_parent);
//    		HashSet<PersonHolder> recipients = new HashSet<PersonHolder>();
//    		ArrayList<GroupHolder> recipient_groups = Sphere.Group.getParents(group.group_id);
//    		recipient_groups.add(group);
//    		for (GroupHolder rg : recipient_groups) {
//    			for (PersonHolder ph : Sphere.Minion.get(rg.group_id, true)) {
//    				recipients.add(ph);
//    			}    			
//    		}
//    		for (PersonHolder ph : recipients) {
//    			p_client.write("notifying " + ph.first_name + "...");    			
//    			XP.dispatch(ph, HMessageBank.getPushGroup(Main.SERVER_PIN, HMI.next(), group));
//    		}
//    	} else {
//    		p_client.write("failed update.");
//    	}
//    	p_client.write("done.");
//	}
//
//	private void onPushImage(Client p_client, WsvBuilder p_wsv) {
//    	String person_id = "ABAB8331";
//		try {
//			if (p_wsv.getToken(2).length() > 0) {
//				person_id = p_wsv.getToken(2);
//			}
//		} catch (Exception e) {
//		}
//        try {
//        	long T0 = System.currentTimeMillis();
//        	PersonHolder ph = Sphere.Person.get(person_id);
//        	if (ph == null) {
//        		p_client.write("unknown person " + person_id);
//        		return;
//        	} else {
//				p_client.write("person : '" + ph.first_name + "'\n connected : '" + ph.connected + "' \n imei : '"
//						+ ph.imei + "' \n offline mode : '" + ph.offline_mode + "'");
//        	}
////            byte[] bImage = MediaUtil.getImage(Main.IMAGE_LOCAL_PATH + "testpushimage.jpg");
//        	StringBuilder sb = new StringBuilder();
//        	for (int i = 0; i < 1000; i++) {
//				sb.append("whats up my niggs");
//			}
//			byte[] bImage = sb.toString().getBytes();
//            p_client.write("get image ..." + Main.IMAGE_LOCAL_PATH + "testpushimage.jpg.  Size = '" + bImage.length + "' bytes");            
//        	HMessage hmessage = new HMessage();
//            hmessage.mCode 	  = HMC.IMAGE_DOWNLOAD;
//            hmessage.mId      = HMI.next();
//            hmessage.mPIN     = ph.f_pin;
//            hmessage.putBody(HMK.FILE_NAME, "testpushimage.jpg");
//            int j = 1000;
//            for (int i = 0; i < 31; i++) {
//            	bImage[++j] = (byte)i;
//            }
//            hmessage.setMedia(bImage);
//            p_client.write("transporting...");            
//            XP.dispatch(ph, hmessage, Queue.A_MINUTE);
//            p_client.write("OK... duration:" + (System.currentTimeMillis() - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write("Exception : " + ex.getMessage() + "\n");
//        }
//    }
//    
//	private void onPushGroup(Client p_client, WsvBuilder p_wsv) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	private static byte[] toMedia(String p_data) {
//		byte[] result = null;
//		if (p_data != null && !p_data.equals("")) {
//			try {
//				result = p_data.getBytes();
//				result = p_data.getBytes("ISO-8859-1");
//			} catch (UnsupportedEncodingException uee) {
//			}
//		}
//		return result;
//	}
//	
//	private static final String CONSOLE_USER = "CONSOLE";
//    private void onSeeWishlist(Client p_client, WsvBuilder p_wsv) {
//    	P: try {
//    		String query = "SELECT ID, CONTENT FROM WISHLIST WHERE USER_PIN = '" + CONSOLE_USER + "'";
//    		ArrayList<Object[]> rs = DB.fetch(query, 20);
//    		StringBuilder sb = new StringBuilder();
//    		for (Object[] row : rs) {
//    			sb.append((int) row[0]).append("\t").append(row[1]).append("\n");
//    		}
//    		p_client.write(sb.toString());
//    	} catch (Exception e) {
//    		Main.getLog().e("onTryQueryWishlist", e);
//    		p_client.write(e.getMessage());
//    	}
//	}
//
//	private void onDelWishlist(Client p_client, WsvBuilder p_wsv) {
//    	P: try {
//    		for(int j = 2; true; j++) {
//    			String wishlist_id = p_wsv.getToken(j);
//    			if (wishlist_id == null) {
//    				break;
//    			}
//    			String query = "DELETE FROM WISHLIST WHERE ID = '" + wishlist_id + "'";
//    			int i = DB.update(query, true);
//    			if (i == 0) {
//    				p_client.write("Wishlist id '" + wishlist_id + "' not found.");
//    			} else if (i == 1) {
//    				p_client.write("Wishlist id '" + wishlist_id + "' deleted.");
//    			}
//    		}
//    	} catch (Exception e) {
//    		Main.getLog().e("onTryQueryWishlist", e);
//    		p_client.write(e.getMessage());
//    	}
//	}
//
//	private void onAddWishlist(Client p_client, WsvBuilder p_wsv) {
//    	P: try {
//    		String wl_text = "";
//    		for (int  i = 2; true; i++) {
//    			String s = p_wsv.getToken(i);
//    			if (s == null) {
//    				break;
//    			} else {
//    				wl_text = wl_text  + " " + s;
//    			}
//    		}
//    		wl_text = wl_text.trim();
//    		String query = "INSERT INTO `WISHLIST` (`USER_PIN`, `CONTENT`, `DATE_SUBMITTED`, `IS_ACTIVE`, `TYPE`, `THUMBNAIL`, `LOCATION`, `LONGITUDE`, `LATITUDE`) VALUES ('" + CONSOLE_USER + "', '" + wl_text + "', "
//    				+ "now(), '0', '99', null, 'DEFAULT', null, null);";
//    		int i = DB.update(query, true);
//    		if (i == 1) {
//    			p_client.write("Inserted.");
//    		} else {
//    			p_client.write("Failed to insert.  Query : " + query);
//    		}
//    	} catch (Exception e) {
//    		
//    		Main.getLog().e("onTryQueryWishlist", e);
//    		p_client.write(e.getMessage());
//    	}
//	}
//
//	private void onTryQueryWishlist(Client p_client, WsvBuilder p_wsv) {
//    	try {
//        	ArrayList<Object[]> 	 wishlist_self	= DB.fetch("SELECT CONTENT FROM WISHLIST WHERE USER_PIN = '" + CONSOLE_USER + "' AND IS_ACTIVE = '1'", 10);
//        	List<Map.Entry<String, Integer>> helper = new ArrayList<Map.Entry<String, Integer>>();
//        	HashMap<String, Integer> simpleRank 	= new HashMap<String, Integer>();
//        	
//        	for (Object[] oWl : wishlist_self) {
//            	String pWish = (String) oWl[0];
//            	pWish = pWish.replace(",", " ").replace(".", " ");        	
//            	List<String> keywords = new ArrayList<String>(Arrays.asList(pWish.split("\\s+")));
//            	StopWordsFilter.getInstance().filter(keywords);
//            	
//            	for (String keyword : keywords) {
//            		String query = "SELECT ID FROM WISHLIST WHERE IS_ACTIVE = 1 AND CONTENT LIKE '%" + keyword + "%' AND USER_PIN <> '" + CONSOLE_USER + "'";
//            		ArrayList<Object[]> data = DB.fetch(query, 20);
//            		for (Object[] row : data) {
//            			String id = (int) row[0] + "";
//            			Integer rank = simpleRank.get(id);
//            			if (rank == null) {
//            				rank = 0; 
//            			}
//            			simpleRank.put(id, ++rank);
//            		}
//            	}
//        	}
//
//        	for (Entry<String, Integer> x : simpleRank.entrySet()) {
//        		helper.add(x);			
//        	}
//        	//code bikin hang aneh
////        	Collections.sort(helper, new Comparator<Map.Entry<String, Integer>>() {
////				@Override
////				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
////		        	Main.getLog().d(TAG, "2 " + o1.getValue() + " " + o2.getValue());
////					return o2.getValue() - o1.getValue();
////				}
////			});
//        	StringBuilder sbResponse = new StringBuilder();
//        	for (Map.Entry<String, Integer> h : helper) {
//        		Object[] wishlist = DB.fetch("SELECT USER_PIN, CONTENT FROM WISHLIST WHERE ID = '" + h.getKey() + "'");
//        		if (wishlist == null) {
//        			Main.getLog().w(TAG, "null wishlist id " + h.getKey());
//        			continue;
//        		}
//        		sbResponse.append(h.getKey()).append("\t").append((String)wishlist[0]).append("  ").append((String)wishlist[1]).append(" score:").append(h.getValue()).append("\n");
//        	}
//			p_client.write(sbResponse.toString());
//    	} catch (Exception e) {
//    		Main.getLog().e("onTryQueryWishlist", e);
//    		p_client.write(e.getMessage());
//    	}
//	}
//
//	private void onSetUserBalance(Client p_client, WsvBuilder p_wsv) {
//    	String ret = "Failed";
//    	P: try {
//    		PersonHolder ph = Sphere.Person.get(p_wsv.getToken(2));
//    		if (ph == null) {
//    			ret = "Unknown person!";
//    			break P;
//    		}
//    		String user_pin = p_wsv.getToken(2);
//    		String balance = p_wsv.getToken(3);
//			String update = "UPDATE WALLET_ACCOUNT SET BALANCE = " + balance
//					+ " WHERE USER_PIN = '" + user_pin + "' AND WALLET_TYPE = '1'";
//			String insert = "INSERT INTO WALLET_ACCOUNT (USER_PIN, WALLET_TYPE, WALLET_NO, BALANCE, SC_DATE) VALUES ('"
//					+ user_pin + "', '1', '" + user_pin + "','" + balance + "', now())";   		
//    		int i = 0;
//                Main.getLog().d(TAG, update +" --> " + i);
//    		if ((i = DB.update(update, true)) < 1) {
//    			i = DB.update(insert, true);
//                        Main.getLog().d(TAG, insert +" --> " + i);
//    		}
//    		if (i <= 0) {
//    			break P;
//    		}
//    		
//                XP.pushBalance(ph, "ControlProcessor-onSetUserBalance");
//    		ret = "Success.";
//    	} catch (Exception e) {
//    		Main.getLog().e("onSetUserBalance", e);
//    	}
//    	p_client.write(ret);
//	}
//
//	private void onExecRemoveFeeder (Client p_client, String p_name) {
////        Feeder feeder = Main.getFeederManager().get(p_name);
////        if (feeder == null) {
////            p_client.write("feeder " + p_name + " not found.\n");
////        }
////        else {
////            if (Main.getFeederManager().remove(feeder)) {
////                p_client.write("remove " + p_name + " success OK.\n");
////            }
////            else {
////                p_client.write("remove " + p_name + " failed.\n");
////            }
////        }
//    }
//    
//    private void onExecAddFeeder (Client p_client, String p_path_jar, String p_class_name) {
////        StringBuilder builder = new StringBuilder();
////        try {
////            Class clazz = new ControlClassLoader().getClassJar(p_class_name, p_path_jar);
////            Feeder feeder = (Feeder) clazz.newInstance();
////            Main.getFeederManager().add(feeder);
////            p_client.write("add " + feeder.getName() + " success OK.\n");
////        } catch (Exception ex) {
////            builder.append(ex.getMessage()).append("\n");
////            StackTraceElement[] stacks = ex.getStackTrace();
////            for (StackTraceElement stack :stacks) {
////                builder.append(stack.getClassName()).append(".").append(stack.getMethodName()).append("(").append(stack.getLineNumber()).append(")\n");
////            }
////            p_client.write(builder.toString());
////        }
//    }
//    
//    private void onExecAddNpm(Client p_client, String p_cell, String p_operator_name, String p_value) {
//        StringBuilder builder = new StringBuilder();
//        try {
//            int value = 0;
//            try {
//                value = Integer.parseInt(p_value);
//            } catch (NumberFormatException numberFormatException) {
//                p_client.write("Invalid '" + p_value + "' for value set value to 0\n");
//            }
//            Sphere.Npm.addData(p_cell, p_operator_name, value);
//            p_client.write("add " + p_value + " to " + p_cell + " success OK.\n");
//        } catch (Exception ex) {
//            builder.append(ex.getMessage()).append("\n");
//            StackTraceElement[] stacks = ex.getStackTrace();
//            for (StackTraceElement stack :stacks) {
//                builder.append(stack.getClassName()).append(".").append(stack.getMethodName()).append("(").append(stack.getLineNumber()).append(")\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    
//    private void onExecStartFeeder() {
////        Main.getFeederManager().start();
//    }
//    private void onExecInterruptFeeder() {
////        Main.getFeederManager().interrupt();
//        try {
//            AppEngine.getInstance().getTask("FeederTask").interrupt();
//        } catch (Exception e) {
//        }
//    }
//    
//    private void onExecStopFeeder() {
////        Main.getFeederManager().stop();
//    }
//    private void onExecDumpOutbox(Client p_client) {
//        Main.execDumpOutbox(p_client);
//    }
//    private void onExecLoadOutbox(Client p_client) {
//        Main.execLoadOutbox(p_client);
//    }
//    private void onExecStartGateways(Client p_client) { 
//        Main.execStartGateways(p_client);
//    }
//    private void onExecStopGateways(final Client p_client) {
//        Main.execStopGateways(p_client);
//    }
//    private void onExecYpRemove(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 3) {
//            String name = p_wsv.getToken(2);
//            int   index = Integer.parseInt(p_wsv.getToken(3));
//            Client client = (NzxClient) Main.getIp2Manager().get(name);
//            if (client == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Client NULL\n");
//                return;
//            }
//            Main.Ip2ClientListener listener = (Main.Ip2ClientListener) client.getClientListener();
//            if (listener == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Ip2ClientListener NULL\n");
//                return;
//            }
//            listener.removeProcessor(index);
//            p_client.write(CODE_SHOW_YP_INFO_0 + "Done... ProcessorSize : " + listener.getIp2Ypm().size() + "\n");
//        }
//    }
//    private void onExecYpAdd(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            String name = p_wsv.getToken(2);
//            Client client = (NzxClient) Main.getIp2Manager().get(name);
//            if (client == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Client NULL\n");
//                return;
//            }
//            Main.Ip2ClientListener listener = (Main.Ip2ClientListener) client.getClientListener();
//            if (listener == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Ip2ClientListener NULL\n");
//                return;
//            }
//            listener.addProcessor();
//            p_client.write(CODE_SHOW_YP_INFO_0 + "Done... ProcessorSize : " + listener.getIp2Ypm().size() + "\n");
//        }
//    }
//    private void onExecLoadPlace(Client p_client) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Sphere.Place.initialize();
//            long T1 = System.currentTimeMillis();
//            p_client.write(CODE_EXEC_LOAD_PLACE_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecLoadProperties(Client p_client) {
//        try {
//            Main.getProperties().load();
//            Main.setPropertiesBuffer();
//            StringBuilder sb = new StringBuilder();
//            sb.append("Load Complete\n");
//            p_client.write(sb.toString());
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecAddVBPlace(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            if (p_wsv.size() > 5) {
//                long T0 = System.currentTimeMillis();
//                String  place_id   = p_wsv.getToken(2);
//                String  date_start = p_wsv.getToken(3);
//                String  date_end   = p_wsv.getToken(4);
//                String  time       = p_wsv.getToken(5);
//
//                //@ get place
//                PlaceHolder place = Sphere.Place.get(place_id);
//                if (place != null) {
//                    //@check 1 cell 1 vb
//                    boolean allowUpdate = true;
//                    ArrayList<Object[]> places = DB.fetch("SELECT PLACE_ID FROM PLACE WHERE CELL = '" + place.cell + "'", -1);
//                    StringBuilder place_ids = new StringBuilder();
//                    for (int i = 0; i < places.size(); i++) {
//                        place_ids.append(",'").append((String) places.get(i)[0]).append("'");
//                    }
//                    
//                    if (!place_ids.toString().equals("")) {
//                        ArrayList<Object[]> objects = DB.fetch("SELECT * "
//                                                         + "FROM VB_STATUS "
//                                                         + "WHERE "
//                                                         + "PLACE_ID IN (" + place_ids.toString().substring(1) + ") "
//                                                         + "AND (STR_TO_DATE('" + date_start + "','%Y-%m-%d') BETWEEN DATE_START AND DATE_END OR STR_TO_DATE('" + date_end + "','%Y-%m-%d') BETWEEN DATE_START AND DATE_END) "
//                                                         + "AND TIME = " + time, -1);
//                        for (int i = 0; i < objects.size(); i++) {
//                            allowUpdate = false;
//                            break;
//                        }
//                    }
//                    
//                    
//                    //@ insert VB_STATUS                    
//                    if (allowUpdate) {
//                        int update = 0;
//                        try {
//                            update = DB.update("INSERT INTO VB_STATUS "
//                                    + "(PLACE_ID, DATE_START, DATE_END, TIME) VALUES "
//                                    + "('" + place_id + "','" + date_start + "','" + date_end + "'," + time + ")", true);
//
//                        } catch (Exception e) {
//                            p_client.write(CODE_EXEC_UPDATE_VB_0 + "No available VB slot for given cell and time | " + e.getMessage());
//                        }
//                        
//                        //@ mirror 
//                        if (update > 0) {
//                            Main.getMirrorManager().broadcast(HMessageBank.getDataAddVBStatus(HMI.next(), place_id, date_start, date_end, time).pack(), true);
//                        }
//                    }
//                    else {
//                        p_client.write(CODE_EXEC_UPDATE_VB_0 + "No available VB slot for given cell and time");
//                    }
//                }
//                
//
//                long T1 = System.currentTimeMillis();
//                p_client.write(CODE_EXEC_UPDATE_VB_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//            }
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//     private void onExecPushPlace(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            String f_pin = p_wsv.getToken(2);
//                try {
//                String requestId = Long.toHexString(System.currentTimeMillis());
//                PersonHolder person = Sphere.Person.get(f_pin);
//                
//                Client client = Main.getClientManager().get(f_pin);
//                if (person != null && client != null) {
//                    ArrayList<PlaceHolder> places = DBVirtual.mCell_Place.get(person.cell);
//                    if (places != null) {
//                        PlaceHolder place;
//                        for (int x = 0; x < places.size(); x++) {
//                            place = places.get(x);
////                            transport(client, person, HMessageBank.getPushPlaceOnCell(place.place_id, place.last_update), Queue.A_MINUTE);
//                            transport(client, person, HMessageBank.getPushPlace(person.f_pin, requestId, place), Queue.A_MINUTE);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecRemoveVBPlace(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            if (p_wsv.size() > 2) {
//                long T0 = System.currentTimeMillis();
//                String  vb_id = p_wsv.getToken(2);
//
//                //@ delete vb
//                int update = 0;
//                try {
//                   update = DB.update("DELETE FROM VB_STATUS WHERE ID = " + vb_id, true);
//                } catch (Exception e) {
//                }
//                //@ mirror
//                if (update > 0) {
//                    Main.getMirrorManager().broadcast(HMessageBank.getDataDeleteVBStatus(HMI.next(), vb_id).pack(), true);
//                }
//
//                long T1 = System.currentTimeMillis();
//                p_client.write(CODE_EXEC_REMOVE_VB_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//            }
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecLoadSN(Client p_client) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Sphere.ServerNotification.initialize();
//            long T1 = System.currentTimeMillis();
//            p_client.write(CODE_EXEC_LOAD_SN_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecBroadcastSN(Client p_client, WsvBuilder p_wsv) {
//        try {
//            if (p_wsv.size() > 2) {
//                // @Step 1:
//                int id = 0;
//                try {
//                    id = Integer.parseInt(p_wsv.getToken(2));
//                } catch (Exception e) {
//                }
//                if (id == 0) {
//                    p_client.write(CODE_EXEC_BROADCAST_SN_0 + "Failed! Invalid id\n");
//                    return;
//                }
//                int count = SN.broadcast(id);
//                if (count >= 0) {
//                    p_client.write(CODE_EXEC_BROADCAST_SN_0 + "Success! Broadcasted to " + count + " users\n");
//                }
//                else {
//                    p_client.write(CODE_EXEC_BROADCAST_SN_0 + "Failed! Error " + count + "\n");
//                }
//            }
//        } catch (Exception e) {
//            p_client.write(e.getMessage() + "\n");
//        }
//    }
//    private void onExecLoadLiveTV(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Sphere.LiveTv.initialize();
//            long T1 = System.currentTimeMillis();
//            p_client.write(CODE_EXEC_LOAD_LIVETV_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecPushLiveTV(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            int tv_id = 0;
//            try {
//                tv_id = Integer.parseInt(p_wsv.getToken(2));
//            } catch (NumberFormatException numberFormatException) {
//            }
//            
//            LiveTvHolder livetv = Sphere.LiveTv.get(tv_id);
//            if (livetv != null) {
//                try {
//                    String[] persons = Sphere.Person.keySet().toArray(new String[0]);
//                    PersonHolder person;
//                    String requestId = Long.toHexString(System.currentTimeMillis());
//                    for (String f_pin : Sphere.Person.keySet().toArray(new String[0])) {
//                        person = Sphere.Person.get(f_pin);
//                        if (person != null) {
//                            HMessage hmessage = new HMessage();
//                            hmessage.mCode = HMC.PUSH_LIVE_TV;
//                            hmessage.mId = HMI.next();
//                            hmessage.mPIN = person.f_pin;
//                            hmessage.setBodies(livetv.toBodies());
//                            hmessage.putBody(HMK.REQUEST_ID, requestId);
//                            transport(p_client, person, hmessage, Queue.A_SECOND);
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecOpenTenderSuggestion(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            
//            //f_pin, subject, suggest description
//            String f_pin = p_wsv.getToken(2);
//            String subject = p_wsv.getToken(3).replaceAll("_", " ");
//            String suggestion = p_wsv.getToken(4).replaceAll("_", " ");
//            
//            try {
//                String requestId = Long.toHexString(System.currentTimeMillis());
//                PersonHolder person = Sphere.Person.get(f_pin);
//                
//                Client client = Main.getClientManager().get(f_pin);
//                if (person != null && client != null) {    
//                    HMessage hMessage = HMessageBank.getPushNewTenderSuggestion(person.f_pin, subject, suggestion);
//                    p_client.write("write..." + hMessage.toLogString());
//                    transport(client, person, hMessage, Queue.A_MINUTE);
//                }
//                else {
//                    long T1 = System.currentTimeMillis();
//                    p_client.write("NOK... Empty person for '"+ f_pin +"'. Duration:" + (T1 - T0) + "ms\n");
//                    return;
//                }
//            } catch (Exception e) {
//                p_client.write(e.getMessage() + "\n");
//                return;
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    
//    private void onExecLoadVod(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Sphere.Vod.initialize();
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecPushVod(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            int vod_id = 0;
//            try {
//                vod_id = Integer.parseInt(p_wsv.getToken(2));
//            } catch (NumberFormatException numberFormatException) {
//            }
//            
//            VodHolder video = Sphere.Vod.get(vod_id);
//            if (video != null) {
//                try {
//                    String[] persons = Sphere.Person.keySet().toArray(new String[0]);
//                    PersonHolder person;
//                    String requestId = Long.toHexString(System.currentTimeMillis());
//                    for (String f_pin : Sphere.Person.keySet().toArray(new String[0])) {
//                        person = Sphere.Person.get(f_pin);
//                        if (person != null) {
//                            HMessage hmessage = new HMessage();
//                            hmessage.mCode = HMC.PUSH_VOD;
//                            hmessage.mId = HMI.next();
//                            hmessage.mPIN = person.f_pin;
//                            hmessage.setBodies(video.toBodies());
//                            hmessage.putBody(HMK.REQUEST_ID, requestId);
//                            transport(p_client, person, hmessage, Queue.A_SECOND);
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecPushAllVod(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            
//            Integer[] vods = Sphere.Vod.keyArray();
//            for (int vod_id : vods) {
//                VodHolder vod = Sphere.Vod.get(vod_id);
//                if (vod != null) {
//                    try {
//                        String[] persons = Sphere.Person.keySet().toArray(new String[0]);
//                        PersonHolder person;
//                        String requestId = Long.toHexString(System.currentTimeMillis());
//                        for (String f_pin : Sphere.Person.keySet().toArray(new String[0])) {
//                            person = Sphere.Person.get(f_pin);
//                            if (person != null) {
//                                HMessage hmessage = new HMessage();
//                                hmessage.mCode = HMC.PUSH_VOD;
//                                hmessage.mId = HMI.next();
//                                hmessage.mPIN = person.f_pin;
//                                hmessage.setBodies(vod.toBodies());
//                                hmessage.putBody(HMK.REQUEST_ID, requestId);
//                                transport(p_client,person, hmessage, Queue.A_SECOND);
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    
//    private void onExecPushRemovedBuddy(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            String l_pin = "";
//            try {
//                l_pin = p_wsv.getToken(2);
//            } catch (NumberFormatException numberFormatException) {
//            }
//            
//            if (!l_pin.equals("")) {
//                try {
//                    String[] persons = Sphere.Person.keySet().toArray(new String[0]);
//                    PersonHolder person;
//                    String requestId = Long.toHexString(System.currentTimeMillis());
//                    for (String f_pin : Sphere.Person.keySet().toArray(new String[0])) {
//                        person = Sphere.Person.get(f_pin);
//                        if (person != null) {
//                            HMessage hmessage = new HMessage();
//                            hmessage.mCode = HMC.DELETE_BUDDY;
//                            hmessage.mPIN = person.f_pin;
//                            hmessage.mBodies.put(HMK.L_PIN, l_pin);
//                            transport(p_client, person, hmessage, Queue.A_SECOND);
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecLoadMessageFilter(Client p_client) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Sphere.MessageFilter.initialize();
//            long T1 = System.currentTimeMillis();
//            p_client.write(CODE_EXEC_LOAD_MESSAGE_FILTER_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//    private void onExecMappingAlias(Client p_client) {
//        try {
//            long T0 = System.currentTimeMillis();
//            Main.getClientManager().get("");
//            
//            long T1 = System.currentTimeMillis();
//            p_client.write(CODE_EXEC_LOAD_SN_0 + "OK... duration:" + (T1 - T0) + "ms\n");
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
//
//      
//    private void onExecPushSync(final Client p_client, WsvBuilder p_wsv) {
//        try {
//            long T0 = System.currentTimeMillis();
//            long t0;
//            String f_pin  = p_wsv.getToken(2);
//            String chat   = p_wsv.getToken(3);
//            try {
//                if (f_pin.equalsIgnoreCase("all")) {
//                    Set<String> keys = Sphere.Person.keySet();
//                    for (String key : keys) {
//                        PersonHolder p = Sphere.Person.get(key);
//                        if (p != null) {
//                            t0 = System.currentTimeMillis();
//                            HMessage hMessage = HMessageBank.getSendInstantMessage("02d6e9a4b3", "admin", p.f_pin, p.user_id, "GreenPages", p.first_name, chat.replaceAll("/n", "\n"));
//                            p_client.write(t0 + " [T1] " + hMessage.toLogString());
//                            String response;
//                            if (Main.getClientManager().get(p.f_pin) != null) {
//                                response = XP.sendRequest(p.f_pin, hMessage, Queue.A_MINUTE);
//                            }
//                            else {
//                                XP.dispatch(p, hMessage, Queue.A_MINUTE);
//                                response = "dispatch";
//                            }
//                            p_client.write(t0 + " [T4] " + (System.currentTimeMillis() - t0) + "ms " + (response == null ? "" : response.replaceAll("\n", "/n")));
//                        }
//                    }
//                }
//                else {
//                    String[] keys = f_pin.split(",");
//                    for (String key : keys) {
//                        PersonHolder p = Sphere.Person.get(key);
//                        if (p != null) {
//                            t0 = System.currentTimeMillis();
//                            HMessage hMessage = HMessageBank.getSendInstantMessage("02d6e9a4b3", "admin", p.f_pin, p.user_id, "GreenPages", p.first_name, chat.replaceAll("/n", "\n"));
//                            p_client.write(t0 + " [T1] " + hMessage.toLogString());
//                            String response = XP.sendRequest(p.f_pin, hMessage, Queue.A_MINUTE);
//                            p_client.write(t0 + " [T4] " + (System.currentTimeMillis() - t0) + "ms " + response.replaceAll("\n", "/n"));
//                        }
//                    }
//                }
//                p_client.write("OK COMPLETE... duration:" + (System.currentTimeMillis() - T0) + "ms\n");
//            } catch (Exception e) {
//                p_client.write("ERROR " + e.getMessage() + "\n");
//                e.printStackTrace();
//            }
//            long T1 = System.currentTimeMillis();
//        } catch (Exception ex) {
//            p_client.write(ex.getMessage() + "\n");
//        }
//    }
////    private void onExecAddBlackUsers(final Client p_client, WsvBuilder p_wsv) {
////        try {
////            long T0 = System.currentTimeMillis();
////            String users = p_wsv.getToken(2);;
////            
////            if (users != null) {
////                try {
////                    Main.addBlackUser(users);
////                } catch (Exception e) {
////                }
////            }
////            
////            long T1 = System.currentTimeMillis();
////            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
////        } catch (Exception ex) {
////            p_client.write(ex.getMessage() + "\n");
////        }
////    }
////    
////    private void onExecRemoveBlackUsers(final Client p_client, WsvBuilder p_wsv) {
////        try {
////            long T0 = System.currentTimeMillis();
////            String users = p_wsv.getToken(2);;
////            
////            if (users != null) {
////                try {
////                    Main.removeBlackUser(users);
////                } catch (Exception e) {
////                }
////            }
////            long T1 = System.currentTimeMillis();
////            p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
////        } catch (Exception ex) {
////            p_client.write(ex.getMessage() + "\n");
////        }
////    }
//       
//    private void onHelp(Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(CODE_HELP_0).append("refresh").append("\n");
//        
//        builder.append(CODE_HELP_0).append("show sphere-size").append("\n");
//        builder.append(CODE_HELP_0).append("show sphere-sn").append("\n");
//        builder.append(CODE_HELP_0).append("show yp-info <name>").append("\n");
//        builder.append(CODE_HELP_0).append("show outbox-size").append("\n");
//        builder.append(CODE_HELP_0).append("show outbox-size <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("show outbox-data-first <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("show outbox-data <f_pin> <message_id>").append("\n");
//        builder.append(CODE_HELP_0).append("show feeder").append("\n");
//        builder.append(CODE_HELP_0).append("show feeder-cd").append("\n");
//        builder.append(CODE_HELP_0).append("show feeder-list").append("\n");
//        builder.append(CODE_HELP_0).append("show chatroom-title").append("\n");
//        builder.append(CODE_HELP_0).append("show chatroom-size").append("\n");
//        builder.append(CODE_HELP_0).append("show roommate-size <id>").append("\n");
//        builder.append(CODE_HELP_0).append("show vcall-profile").append("\n");
//        builder.append(CODE_HELP_0).append("show voip-profile").append("\n");
//        builder.append(CODE_HELP_0).append("show cell-persons <cell>").append("\n");
//        builder.append(CODE_HELP_0).append("show cell-vb-status <latitude> <longitude>").append("\n");
//        builder.append(CODE_HELP_0).append("show person <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("show buddies <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("show buddies-online-status").append("\n"); // GANTI show buddies-online-count
//        builder.append(CODE_HELP_0).append("show group-members <group_id>").append("\n");
//        builder.append(CODE_HELP_0).append("show user-alias <user_id>").append("\n");
//        
//        builder.append(CODE_HELP_0).append("exec add-feeder <path> <class_name>").append("\n");
//        builder.append(CODE_HELP_0).append("exec add-npm <cell> <operator_name> <value>").append("\n");
//        builder.append(CODE_HELP_0).append("exec yp-add <name>").append("\n");
//        builder.append(CODE_HELP_0).append("exec yp-remove <name> <index>").append("\n");
//        builder.append(CODE_HELP_0).append("exec dump-outbox").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-outbox").append("\n");
//        builder.append(CODE_HELP_0).append("exec start-server").append("\n");
//        builder.append(CODE_HELP_0).append("exec kill-server").append("\n");
//        builder.append(CODE_HELP_0).append("exec start-gateways").append("\n");
//        builder.append(CODE_HELP_0).append("exec stop-gateways").append("\n");
//        builder.append(CODE_HELP_0).append("exec query <query>").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-place").append("\n");
//        builder.append(CODE_HELP_0).append("exec remove-feeder <class_name>").append("\n");
//        builder.append(CODE_HELP_0).append("exec start-feeder").append("\n");
//        builder.append(CODE_HELP_0).append("exec interrupt-feeder").append("\n");
//        builder.append(CODE_HELP_0).append("exec stop-feeder").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-properties").append("\n");
//        builder.append(CODE_HELP_0).append("exec add-vb <place_id> <date_start> <date_end> <time>").append("\n");
//        builder.append(CODE_HELP_0).append("exec remove-vb <vb_id>").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-livetv").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-livetv").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-sn").append("\n");
//        builder.append(CODE_HELP_0).append("exec broadcast-sn <id>").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-vod").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-vod <vod_id>").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-vod-all").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-removed-buddy <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("exec add-black-users <userid_1,userid_2,..>").append("\n");
//        builder.append(CODE_HELP_0).append("exec remove-black-users <userid_1,userid_2,..>").append("\n");
//        builder.append(CODE_HELP_0).append("exec outbox-reset <f_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("exec load-message-filter").append("\n");
//        builder.append(CODE_HELP_0).append("exec remove-alias <user_id>").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-place <user_id>").append("\n");
//        builder.append(CODE_HELP_0).append("exec suggest-open-tender <f_pin> <subject> <suggestion message>").append("\n");
//        builder.append(CODE_HELP_0).append("exec set-user-balance <f_pin> <balance>").append("\n");
//        builder.append(CODE_HELP_0).append("exec try-query-wishlist <query>").append("\n");
//        builder.append(CODE_HELP_0).append("exec see-wl").append("\n");
//        builder.append(CODE_HELP_0).append("exec add-wl {<wish_text>+}").append("\n");
//        builder.append(CODE_HELP_0).append("exec del-wl {<wish_id>+").append("\n");
//        builder.append(CODE_HELP_0).append("exec wl-matches").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-image <person_id default 'ABAB8331'> (default )").append("\n");
//        builder.append(CODE_HELP_0).append("exec switch-org-parent <user_pin>  <new_parent_pin>").append("\n");
//        builder.append(CODE_HELP_0).append("exec push-sync <f_pin> <l_user_id>  <chat>").append("\n");
//        
//        builder.append(CODE_HELP_0).append("set put-alias <user_id> <alias>").append("\n");
//        builder.append(CODE_HELP_0).append("set comlib-debug <boolean>").append("\n");
//        builder.append(CODE_HELP_0).append("set log-level <name> <int>").append("\n");
//        builder.append(CODE_HELP_0).append("set archieve-interval <millis> <table_name>").append("\n");
//        builder.append(CODE_HELP_0).append("set comlib-outbox").append("\n");
//        
//        p_client.write(builder.toString());        
//    }
//        
//    private void onShow(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.getToken(1).equals("sphere-size")) {
//            onShowSphereSize(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("sphere-sn")) {
//            onShowSphereSN(p_client);
//        }
////        else if (p_wsv.getToken(1).equals("db-sn")) {
////            onShowDbSN(p_client);
////        }
//        else if (p_wsv.getToken(1).equals("yp-info")) {
//            onShowYpInfo(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("outbox-size")) {
//            onShowOutboxSize(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("outbox-data-first")) {
//            onShowOutboxDataFirst(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("outbox-data")) {
//            onShowOutboxData(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("feeder")) {
//            onShowFeeder(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("feeder-list")) {
//            onShowFeederList(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("feeder-cd")) {
//            onShowFeederCD(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("chatroom-titles")) {
//            onShowChatroomTitles(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("chatroom-size")) {
//            onShowChatroomSize(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("roommate-size")) {
//            onShowRoommateSize(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("vcall-profile")) {
//            onShowVCallProfile(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("voip-profile")) {
//            onShowVoIPProfile(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("cell-persons")) {
//            onShowCellPersons(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("person")) {
//            onShowPerson(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("person-by-msisdn")) {
//            onShowPersonByMsisdn(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("cell-vb-status")) {
//            onShowCellVBStatus(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("buddies")) {
//            onShowBuddies(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("group-members")) {
//            onShowGroupMembers(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("buddies-online-status")) {
//            onShowBuddiesOnlineStatus(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("black-users")) {
//            onShowBlackUsers(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("user-alias")) {
//            onShowAlias(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("cache")) {
//            onShowCache(p_client, p_wsv);
//        }
//    }  
//    private void onShowCache(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            String result = "";
//            String cache_key  = p_wsv.getToken(2);
//            String index_key  = p_wsv.getToken(3);
//            String object_key = p_wsv.getToken(4);
//            Cache cache = AppEngine.getInstance().getInCache().getCache(cache_key);
//            if (cache == null) {
//                result = "Cache NULL";
//            }
//            else {
//                Object o = cache.getHolder(Integer.valueOf(index_key), object_key);
//                if (o == null) {
//                    result = "Object NULL";
//                }
//                else {
//                    result = o.toString();
//                }
//            }
//            p_client.write(result + "\n");
//        }
//    }
//    private void onShowGroupMembers(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            String group_id = p_wsv.getToken(2);
//            ArrayList<PersonHolder> persons = Sphere.Minion.get(group_id, false);
//            if (persons != null && !persons.isEmpty()) {
//                PersonHolder person;
//                for (int x = 0; x < persons.size(); x++) {
//                    person = persons.get(x);
//                    builder.append(CODE_SHOW_GROUP_MEMBERS_0).append(x).append(".").append(person.f_pin).append(";").append(person.user_id).append("\n");
//                }
//            }
//            else {
//                builder.append(CODE_SHOW_GROUP_MEMBERS_0).append("Group Members NOK").append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowBuddies(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            String f_pin = p_wsv.getToken(2);
//            ArrayList<PersonHolder> persons = Sphere.Buddy.get(f_pin, false);
//            if (persons != null && !persons.isEmpty()) {
//                PersonHolder person;
//                for (int x = 0; x < persons.size(); x++) {
//                    person = persons.get(x);
//                    builder.append(CODE_SHOW_BUDDIES_0).append(x).append(". ").append(person.f_pin).append(";").append(person.user_id).append("\n");
//                }
//            }
//            else {
//                builder.append(CODE_SHOW_BUDDIES_0).append("Buddies NOK").append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowPersonByMsisdn(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            String msisdn = p_wsv.getToken(2);
//            ArrayList<PersonHolder> persons = DBVirtual.MSISDN.get(msisdn, false);
//            if (persons == null || persons.isEmpty()) {
//                builder.append(CODE_SHOW_PERSON_1).append("Person(s) NOK").append("\n");
//            }
//            else {
//                for (int x = 0; x < persons.size(); x++) {
//                    builder.append(toPersonString(CODE_SHOW_PERSON_1, persons.get(x)));
//                }
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowPerson(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            String f_pin = p_wsv.getToken(2);
//            PersonHolder person = Sphere.Person.get(f_pin);
//            if (person == null) {
//                builder.append(CODE_SHOW_PERSON_0).append("Person NOK").append("\n");
//            }
//            else {
//                builder.append(toPersonString(CODE_SHOW_PERSON_0, person));
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private String toPersonString(String p_tag, PersonHolder p_person) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(p_tag).append("f_pin        = ").append(p_person.f_pin).append("\n");
//        builder.append(p_tag).append("first_name   = ").append(p_person.first_name).append("\n");
//        builder.append(p_tag).append("user_id      = ").append(p_person.user_id).append("\n");
//        builder.append(p_tag).append("imei         = ").append(p_person.imei).append("\n");
//        builder.append(p_tag).append("email        = ").append(p_person.email).append("\n");
//        builder.append(p_tag).append("msisdn       = ").append(p_person.msisdn).append("\n");
//        builder.append(p_tag).append("upline_pin   = ").append(p_person.upline_pin).append("\n");
//        builder.append(p_tag).append("connected    = ").append(p_person.connected).append("\n");
//        builder.append(p_tag).append("created_date = ").append(Util.getTransDate(p_person.created_date)).append("\n");
//        builder.append(p_tag).append("last_upd     = ").append(Util.getTransDate(p_person.last_update)).append("\n");
//        builder.append(p_tag).append("latitude     = ").append(p_person.latitude).append("\n");
//        builder.append(p_tag).append("longitude    = ").append(p_person.longitude).append("\n");
//        builder.append(p_tag).append("altitude     = ").append(p_person.altitude).append("\n");
//        builder.append(p_tag).append("cell         = ").append(p_person.cell).append("\n");
//        builder.append(p_tag).append("last_loc_upd = ").append(Util.getTransDate(p_person.last_loc_update)).append("\n");
//        builder.append(p_tag).append("privacy_flag = ").append(p_person.privacy_flag).append("\n");
//        return builder.toString();        
//    }
//    private void onShowCellPersons(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            String cell = p_wsv.getToken(2);
//                   cell = cell.equals("empty") ? "" : cell;
//            ArrayList<PersonHolder> persons = DBVirtual.getPersonsAtCell(cell, true);
//            for (int x = 0; x < persons.size(); x++) {
//                PersonHolder person = persons.get(x);
//                builder.append(CODE_SHOW_CELL_PERSONS_0).append(person.f_pin).append(":").append(person.user_id).append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowChatroomSize(Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(CODE_SHOW_CHATROOM_SIZE_0).append(Sphere.ChatRoom.size()).append("\n");
//        p_client.write(builder.toString());
//    }
//    private void onShowChatroomTitles(Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        String[] keys = Sphere.ChatRoom.keyArray();
//        for (int x = 0; x < keys.length; x++) {
//            try {
//                String key = keys[x];
//                ChatRoomHolder chatroom = Sphere.ChatRoom.get(key);
//                if (chatroom != null) {
//                    builder.append(CODE_SHOW_CHATROOM_TITLES_0).append(x + 1).append(". ").append(chatroom.chatroom_id).append(" ").append(chatroom.title).append("\n");
//                }
//            } catch (Exception e) {
//            }
//        }
//        p_client.write(builder.toString());
//    }
//    private void onShowRoommateSize(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            builder.append(CODE_SHOW_ROOMMATE_SIZE_0).append(DBVirtual.CHATROOM.MATE.sizeOf(p_wsv.getToken(2))).append("\n");
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowBuddiesOnlineStatus(Client p_client, WsvBuilder p_wsv) {
//        long T0 = System.currentTimeMillis();
//        int mOnline = 0;
//        int mOffline = 0;
//
//        PersonHolder person;
//        String[] persons = Sphere.Person.keySet().toArray(new String[0]);
//        for (String f_pin : persons) {
//            person = Sphere.Person.get(f_pin);
//            if (person != null) {
//                if (person.connected.equals(PersonHolder.CONNECTED)) {
//                    mOnline++;
//                }
//                else {
//                    mOffline++;
//                }
//            }
//        }
//        
//        p_client.write("User Online Status");
//        p_client.write(new Date().toLocaleString());
//        p_client.write("Online  : " + mOnline + "");
//        p_client.write("Offline : " + mOffline + "");
//
//        long T1 = System.currentTimeMillis();
//        p_client.write("OK... duration:" + (T1 - T0) + "ms\n");
//    }
//    
//    private void onShowFeederList(Client p_client) {
////        FeederManager feederManager = Main.getFeederManager();
////        StringBuilder builder = new StringBuilder();
////        int i=0;
////        while (true) {
////            try {
////                Feeder feeder = feederManager.get(i++);
////                builder.append("name: ").append(feeder.getName()).append(" | url: ").append(feeder.getUrl()).append("\n");
////            } catch (Exception e) {
////                break;
////            }
////        }
////        
////        p_client.write(builder.toString());
//    }
//    
//    private void onShowFeeder(Client p_client) {
////        ItemPool pool = Main.getFeederManager().getItemPool();
////        StringBuilder builder = new StringBuilder();
////        for (int x = 0; x < pool.size(); x++) {
////            Item item = pool.get(x);
////            builder.append(CODE_SHOW_FEEDER_0).append(x + 1).append(". ").append(new Date(item.getPubDate()).toString()).append("\t").append(item.getResource()).append(" - ").append(item.getTitle()).append("\n");
////        }
////        p_client.write(builder.toString());
//    }
//    private void onShowFeederCD(Client p_client) {
////        StringBuilder builder = new StringBuilder();
////        long time = Main.getFeederManager().getWakeupTime();
////        if (time > 0L) {
////            long cd = time - System.currentTimeMillis();
////                 cd = cd / 60000;
////            builder.append(CODE_SHOW_FEEDER_CD_0).append(cd).append(" minute(s)").append("\n");            
////        }
////        else {
////            builder.append(CODE_SHOW_FEEDER_CD_0).append("0 minute(s)").append("\n");
////        }
////        p_client.write(builder.toString());
//    }
//    private void onShowOutboxData(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 3) {
//            StringBuilder builder = new StringBuilder();
//            try {
//                QueueList queues = Main.getPostman().get(p_wsv.getToken(2), false);
//                if (queues != null && queues.size() > 0) {
//                    Queue queue = queues.get(p_wsv.getToken(3));
//                    String message = queue.getHMessage().pack();
//                    long cd = queue.getCreatedDate();
//                    long ed = queue.getEndDate();
//                    long lu = queue.getLastUpdate();                    
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Created Date : ").append(cd).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("End Date     : ").append(ed).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Last Update  : ").append(lu).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Data         : ").append(message).append("\n");
//                }
//                else {
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Empty").append("\n");
//                }
//            } catch (Exception e) {
//                builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Exception!!!").append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowOutboxDataFirst(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            StringBuilder builder = new StringBuilder();
//            try {
//                ArrayList<Queue> queues = Main.getPostman().get(p_wsv.getToken(2), false);
//                if (queues != null && queues.size() > 0) {
//                    Queue queue = queues.get(0);
//                    String message = queue.getHMessage().pack();
//                    long cd = queue.getCreatedDate();
//                    long ed = queue.getEndDate();
//                    long lu = queue.getLastUpdate();                    
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Created Date : ").append(cd).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("End Date     : ").append(ed).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Last Update  : ").append(lu).append("\n");
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Data         : ").append(message).append("\n");
//                }
//                else {
//                    builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Empty").append("\n");
//                }
//            } catch (Exception e) {
//                builder.append(CODE_SHOW_OUTBOX_DATA_0).append("Exception!!!").append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowOutboxSize(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {            
//            long computeQueueCount = Main.getPostman().computeQueueCount(p_wsv.getToken(2));
//            StringBuilder builder = new StringBuilder();
//            builder.append(CODE_SHOW_OUTBOX_SIZE_0).append("Compute : ").append(computeQueueCount).append("\n");
//            p_client.write(builder.toString());
//        }
//        else {
////            long computeQueueCount = Main.getPostman().computeQueueCount();
//            long currentQueueCount = Main.getPostman().getQueueCount();
//            long keySize = Main.getPostman().keySet().size();
//            StringBuilder builder = new StringBuilder();
////            builder.append(CODE_SHOW_OUTBOX_SIZE_0).append("Compute : ").append(computeQueueCount).append("\n");
//            builder.append(CODE_SHOW_OUTBOX_SIZE_0).append("Current : ").append(currentQueueCount).append("\n");
//            builder.append(CODE_SHOW_OUTBOX_SIZE_0).append("KeySize : ").append(keySize).append("\n");
//        }
//    }
//    private void onResetOutbox(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {            
//            StringBuilder builder = new StringBuilder();
//            long computeQueueCount = Main.getPostman().computeQueueCount(p_wsv.getToken(2));
//            while(computeQueueCount > 0) {
//                Main.getPostman().get(p_wsv.getToken(2), false).remove(0);
//                computeQueueCount = Main.getPostman().computeQueueCount(p_wsv.getToken(2));
//                builder.append(CODE_EXEC_OUTBOX_RESET_0).append("Compute : ").append(computeQueueCount).append("\n");
//            }
//            p_client.write(builder.toString());
//        }
//        else {
//            long computeQueueCount = Main.getPostman().computeQueueCount();
//            long currentQueueCount = Main.getPostman().getQueueCount();
//            long keySize = Main.getPostman().keySet().size();
//            StringBuilder builder = new StringBuilder();
//            builder.append(CODE_EXEC_OUTBOX_RESET_0).append("Removed : ").append(computeQueueCount).append("\n");
//            builder.append(CODE_EXEC_OUTBOX_RESET_0).append("Current : ").append(currentQueueCount).append("\n");
//            builder.append(CODE_EXEC_OUTBOX_RESET_0).append("KeySize : ").append(keySize).append("\n");
//            p_client.write(builder.toString());
//        }
//    }
//    
//    private void onShowYpInfo(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            String name = p_wsv.getToken(2);
//            Client client = (NzxClient) Main.getIp2Manager().get(name);
//            if (client == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Client NULL\n");
//                return;
//            }
//            Main.Ip2ClientListener listener = (Main.Ip2ClientListener) client.getClientListener();
//            if (listener == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "Ip2ClientListener NULL\n");
//                return;
//            }
//            YPM ypm = listener.getIp2Ypm();
//            if (ypm == null) {
//                p_client.write(CODE_SHOW_YP_INFO_0 + "YPM NULL\n");
//                return;
//            }
//            StringBuilder builder = new StringBuilder();
//            int size = ypm.size();
//            builder.append(CODE_SHOW_YP_INFO_1).append(size).append(",").append(Main.getIp2Xpm().getProcessCount()).append("\n");
//            for (int x = 0; x < size; x++) {
//                try {
//                    YP yp = ypm.getProcessor(x);
//                    builder.append(CODE_SHOW_YP_INFO_2).append(x).append(",");
//                    builder.append(yp.getCurrentSize()) .append(",").append(yp.getLastSize())   .append(",").append(yp.getTopSize()).append(",");
//                    builder.append(yp.getLastDuration()).append(",").append(yp.getTopDuration()).append("\n");
//                } catch (Exception | Error e) {
//                }
//            }
//            p_client.write(builder.toString());
//        }
//    }
//    private void onShowSphereSize(Client p_client) {
//        StringBuilder builder = new StringBuilder();    
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.App    : ").append(Sphere.App.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.Person : ").append(Sphere.Person.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.Group  : ").append(Sphere.Group.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.Family : ").append(Sphere.Family.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.Place  : ").append(Sphere.Place.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("M.Npm    : ").append(Sphere.Npm.size()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("S.Buddy  : ").append(Sphere.Buddy.keySize()).append("/").append(Sphere.Buddy.valueSize()).append("\n");
//        builder.append(CODE_SHOW_SPHERE_SIZE_0).append("S.Minion : ").append(Sphere.Minion.keySize()).append("/").append(Sphere.Minion.valueSize()).append("\n");
//        p_client.write(builder.toString());
//    }
//    
//    
//    private void onShowVCallProfile(Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        ArrayList<Profile> list = DBVirtual.PROFILE.get("vcall", true);
//        if (list != null) {
//            for (int x = 0; x < list.size(); x++) {
//                VCallProfile vcall = (VCallProfile) list.get(x);
//                String data = new StringBuilder()
//                .append(vcall.device_brand)
//                .append(vcall.device_model)
//                .append(vcall.status)
//                .append(vcall.wifi_status)
//                .append(vcall.operator_name)
//                .append(vcall.sdk_int).toString();
//                builder.append(CODE_SHOW_VCALL_PROFILE_0).append(data).append("\n");
//            }
//        }   
//        if (builder.length() == 0) {
//            builder.append(CODE_SHOW_VCALL_PROFILE_0).append("Empty").append("\n");
//        }
//        p_client.write(builder.toString());        
//    }
//    
//    private void onShowVoIPProfile(Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        ArrayList<Profile> list = DBVirtual.PROFILE.get("voip", true);
//        if (list != null) {
//            for (int x = 0; x < list.size(); x++) {
//                VoIPProfile voip = (VoIPProfile) list.get(x);
//                String data = new StringBuilder()
//                .append(voip.device_brand)
//                .append(voip.device_model)
//                .append(voip.status)
//                .append(voip.wifi_status)
//                .append(voip.operator_name)
//                .append(voip.sdk_int).toString();
//                builder.append(CODE_SHOW_VOIP_PROFILE_0).append(data).append("\n");
//            }
//        }    
//        if (builder.length() == 0) {
//            builder.append(CODE_SHOW_VOIP_PROFILE_0).append("Empty").append("\n");
//        }
//        p_client.write(builder.toString());        
//    }
//    
//    private void onShowCellVBStatus(final Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 3) {
//            try {
//                String lat = p_wsv.getToken(2);
//                String lng = p_wsv.getToken(3);
//                
//                Cell cell = new Cell();
//                cell.unpack(lat, lng);
//                
//                ArrayList<Object[]> obj = DB.fetch("SELECT PLACE_ID FROM PLACE WHERE CELL = '" + cell.getCell() + "'", -1);
//                StringBuilder places = new StringBuilder();
//                String place_id = "";
//                PlaceHolder place;
//                for (int i = 0; i < obj.size(); i++) {
//                    place_id = (String) obj.get(i)[0];
//                    place = Sphere.Place.get(place_id);
//                    if (place != null) {
//                        p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + place.place_name + "(" + place_id + ")  | Current VB visibility --> " + (place.vb_status == 1));
//                    }
//                    places.append(",'").append(place_id).append("'");
//                }
//                p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + "");
//                if (obj.size() > 0) {
//                    ArrayList<Object[]> vb_statuses = DB.fetch("SELECT ID,PLACE_ID,DATE_START,DATE_END,`TIME` FROM VB_STATUS WHERE PLACE_ID IN (" + places.toString().substring(1) + ") ORDER BY PLACE_ID, DATE_START, DATE_END, `TIME`", -1);
//                    long id;
//                    Date dateStart;
//                    Date dateEnd;
//                    String placeId;
//                    int time;
//                    for (int i = 0; i < vb_statuses.size(); i++) {
//                        try {
//                            id          = (Long)    vb_statuses.get(i)[0];
//                            placeId     = (String)  vb_statuses.get(i)[1];
//                            dateStart   = (Date)    vb_statuses.get(i)[2];
//                            dateEnd     = (Date)    vb_statuses.get(i)[3];
//                            time        = (Integer) vb_statuses.get(i)[4];                            
//                            place = Sphere.Place.get(placeId);
//                            if (place != null) {
//                                p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + id + " | " + place.place_name + "(" + placeId + ") | From '" + dateStart.toString() + "' to '" + dateEnd.toString() + "' at '" + time + "'");
//                            }
//                        } catch (Exception e) {
//                            Main.getLog().e(TAG, "onShowCellVBStatus", e);
//                            p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + e.getMessage());
//                        }
//                    }
//                    if (vb_statuses.isEmpty()) {
//                        p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + "No VB found");
//                    }
//                }
//                else {
//                    p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + "No place found");
//                }
//            } catch (Exception e) {
//                p_client.write(CODE_SHOW_CELL_VB_STATUS_0 + e.getMessage());
//            }
//        }
//    }
//    
//    private void onShowSphereSN(final Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        ServerNotificationHolder holder = Sphere.ServerNotification.getVersionHolder();
//        if (holder.getId() != 0L) {
//            builder .append(CODE_SHOW_SPHERE_SN_0)
//                    .append(holder.getId()).append(" ")
//                    .append(holder.getStatus()).append(" ")
//                    .append(holder.getKey()).append(" ")
//                    .append(holder.getValue()).append(" ")
//                    .append(holder.getMessageText()).append("\n");
//        }
//        if (builder.length() == 0) {
//            builder.append(CODE_SHOW_SPHERE_SN_0).append("SN Empty").append("\n");
//        }
//        p_client.write(builder.toString());        
//    }
//    
//    private void onShowBlackUsers(final Client p_client) {
//        StringBuilder builder = new StringBuilder();
//        String[] temp = Main.BL4CK_USERS;
//        if (temp.length == 0) {            
//             builder.append(CODE_SHOW_BLACK_USER_0).append("Empty").append("\n");
//        }
//        else {
//            builder.append(CODE_SHOW_BLACK_USER_0);
//            for (String user : temp) {
//                builder.append(user);
//            }
//            builder.append("\n");
//        }
//        p_client.write(builder.toString());        
//    }
//    
//    private void onSet(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.getToken(1).equals("archieve-interval")) {
////            onSetArchieveInterval(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("archieve-save")) {
////            onSetSaveArchieved(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("archieve-get")) {
////            onSetGetArchieved(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("log-level")) {
//            onSetLogLevel(p_client, p_wsv);
//        }
//        else if (p_wsv.getToken(1).equals("comlib-outbox")) {
//            onSetComlibOutbox(p_client);
//        }
//        else if (p_wsv.getToken(1).equals("put-alias")) {
//            onSetAlias(p_client, p_wsv);
//        }
////        else if (p_wsv.getToken(1).equals("bcast-notif")) {
////            onSetBcastNotif(p_client, p_wsv);
////        }
//    }
//    
//    private void onSetComlibOutbox(Client p_client) {
////        JClsServer.getInstance().setOutboxEnabled();
////        p_client.write(CODE_SET_COMLIB_OUTBOX_0 + "Success\n");
//    }
//    
//    private void onSetLogLevel(Client p_client, WsvBuilder p_wsv) {
//        if (p_wsv.size() > 2) {
//            int level = Log.DEBUG;
//            try {
//                level = Integer.parseInt(p_wsv.getToken(2));
//            } catch (Exception e) {
//            }
//            String result = "";
//            switch (level) {
//                case Log.DEBUG:
//                case Log.INFO:
//                case Log.WARNING:
//                case Log.ERROR:
//                    result = CODE_SET_LOG_LEVEL_0 + " Success\n";
//                    Main.getLog().setLevel(level);
//                    break;
//                default:
//                    break;        
//            }
//            p_client.write(result);
//        }
//    }
//    
//    
//    
//    /*
//        ALIAS
//    */
//     private void onShowAlias(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 2) {            
////            StringBuilder builder = new StringBuilder();
////            
////            String[] alias = JClsServer.getInstance().getAlias(p_wsv.getToken(2));
////            builder.append(CODE_SHOW_USER_ALIAS_0).append("SOURCE : ").append(p_wsv.getToken(2)).append("\n");
////            builder.append(CODE_SHOW_USER_ALIAS_0).append("USERID_ALIAS_MAP : ").append(alias[0] == null ? "-" : alias[0]).append("\n");
////            builder.append(CODE_SHOW_USER_ALIAS_0).append("ALIAS_USERID_MAP : ").append(alias[1] == null ? "-" : alias[1]).append("\n");
////            
////            p_client.write(builder.toString());
////        }
//    }
//
//    private void onSetAlias(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 3) {
////            StringBuilder builder = new StringBuilder();
////
////            JClsServer.getInstance().putAlias(p_wsv.getToken(2), p_wsv.getToken(3));
////            String[] alias = JClsServer.getInstance().getAlias(p_wsv.getToken(2));
////            builder.append(CODE_EXEC_SET_USER_ALIAS_0).append("SOURCE : ").append(p_wsv.getToken(2)).append("\n");
////            builder.append(CODE_EXEC_SET_USER_ALIAS_0).append("USERID_ALIAS_MAP : ").append(alias[0] == null ? "-" : alias[0]).append("\n");
////            builder.append(CODE_EXEC_SET_USER_ALIAS_0).append("ALIAS_USERID_MAP : ").append(alias[1] == null ? "-" : alias[1]).append("\n");
////
////            p_client.write(builder.toString());
////        }
//    }
//
//    private void onExecRemoveAlias(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 3) {
////            StringBuilder builder = new StringBuilder();
////
////            JClsServer.getInstance().removeAlias(p_wsv.getToken(2));
////            String[] alias = JClsServer.getInstance().getAlias(p_wsv.getToken(2));
////            builder.append(CODE_EXEC_REMOVE_USER_ALIAS_0).append("SOURCE : ").append(p_wsv.getToken(2)).append("\n");
////            builder.append(CODE_EXEC_REMOVE_USER_ALIAS_0).append("USERID_ALIAS_MAP : ").append(alias[0] == null ? "-" : alias[0]).append("\n");
////            builder.append(CODE_EXEC_REMOVE_USER_ALIAS_0).append("ALIAS_USERID_MAP : ").append(alias[1] == null ? "-" : alias[1]).append("\n");
////
////            p_client.write(builder.toString());
////        }
//    }
////    private void onSetArchieveInterval(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 2) {
////            try {
////                long interval = Long.parseLong(p_wsv.getToken(2));
////                Archieve.addFilter(interval, p_wsv.getToken(3));
////                p_client.write("Set " + interval + " Done\n");
////            } catch (Exception e) {
////            }
////        }
////    }
////    
////    private void onSetSaveArchieved(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 2) {
////            try {
////                boolean archieved = Archieve.saveArchieved(p_wsv.getToken(2), p_wsv.getToken(3), System.currentTimeMillis(), System.currentTimeMillis()+ (Archieve.DAY * 7));
////                p_client.write("result : " + archieved + "\n");
////            } catch (Exception e) {
////                p_client.write("Set Exception :" + e.getMessage()  + "\n");
////                e.printStackTrace();
////            }
////        }
////    }
////    
////    private void onSetGetArchieved(Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 2) {
////            try {
////                StringBuilder sb = new  StringBuilder();
////                ArrayList<String> data = Archieve.getArchieved(p_wsv.getToken(2));
////                for (int i = 0; i < data.size(); i++) {
////                    sb.append(data.get(i)).append(" \n");
////                }
////                sb.append("size : " + data.size() + "\n");
////                p_client.write(sb.toString());
////            } catch (Exception e) {
////                p_client.write("Set Exception :" + e.getMessage()  + "\n");
////                e.printStackTrace();
////            }
////        }
////    }
//    
////    private void onSetBcastNotif(final Client p_client, WsvBuilder p_wsv) {
////        if (p_wsv.size() > 3) {
////            try {
////                long T0 = System.currentTimeMillis();
////                
////                String id       = p_wsv.getToken(2);
////                String status   = p_wsv.getToken(3);
////                
////                try {
////                    DB.update("UPDATE BROADCAST_NOTIFICATION SET STATUS = " + status + " WHERE ID = " + id, true);
////                } catch (Exception e) {
////                    p_client.write(CODE_EXEC_SET_BCAST_NOTIF_2 + "NOK... Update fail\n");
////                }
////                
////                long T1 = System.currentTimeMillis();
////                p_client.write(CODE_EXEC_SET_BCAST_NOTIF_2 + "OK... duration:" + (T1 - T0) + "ms\n");
////            } catch (Exception e) {
////                p_client.write(CODE_EXEC_SET_BCAST_NOTIF_2 + "NOK... " + e.getMessage());
////                e.printStackTrace();
////            }
////        }
////    }  
//    
//    
//    
//    public static final String CODE_SHOW_SPHERE_SIZE_0 = "AB0> ";
//    public static final String CODE_SHOW_YP_INFO_0 = "AA0> ";
//    public static final String CODE_SHOW_YP_INFO_1 = "AA1> ";
//    public static final String CODE_SHOW_YP_INFO_2 = "AA2> ";
//    public static final String CODE_HELP_0 = "AC0> ";
//    public static final String CODE_REFRESH_0 = "AD0> ";
//    public static final String CODE_EXEC_DUMP_OUTBOX_0 = "AE0> ";
//    public static final String CODE_EXEC_START_GATEWAYS_0 = "AF0> ";
//    public static final String CODE_EXEC_STOP_GATEWAYS_0 = "AG0> ";
//    public static final String CODE_SHOW_OUTBOX_SIZE_0 = "AH0> ";
//    public static final String CODE_SHOW_IP2XPM_ACTIVE_SIZE_0 = "AI0> ";
//    public static final String CODE_SHOW_FEEDER_0    = "AJ0> ";
//    public static final String CODE_SHOW_FEEDER_CD_0 = "AK0> ";
//    public static final String CODE_SHOW_CHATROOM_SIZE_0 = "AL0> ";
//    public static final String CODE_SHOW_CHATROOM_TITLES_0 = "AM0> ";
//    public static final String CODE_SHOW_ROOMMATE_SIZE_0 = "AN0> ";
//    public static final String CODE_SHOW_VCALL_PROFILE_0 = "AO0> ";
//    public static final String CODE_SHOW_VOIP_PROFILE_0 = "AP0> ";
//    public static final String CODE_SHOW_CELL_PERSONS_0 = "AQ0> ";
//    public static final String CODE_SHOW_PERSON_0 = "AR0> ";
//    public static final String CODE_SHOW_PERSON_1 = "AR1> ";
//    public static final String CODE_EXEC_LOAD_PLACE_0 = "AS0> ";
//    public static final String CODE_EXEC_LOAD_VOD_0 = "AS1> ";
//    public static final String CODE_EXEC_LOAD_LIVETV_0 = "AS2> ";
//    public static final String CODE_EXEC_UPDATE_VB_0 = "AT0> ";
//    public static final String CODE_SHOW_CELL_VB_STATUS_0 = "AU0> ";
//    public static final String CODE_EXEC_REMOVE_VB_0 = "AV0> ";
//    public static final String CODE_SHOW_BUDDIES_0 = "AW0> ";
//    public static final String CODE_SHOW_GROUP_MEMBERS_0 = "AX0> ";
////    public static final String CODE_SHOW_DB_SN_0 = "AY0> ";
//    public static final String CODE_EXEC_ADD_BCAST_NOTIF_1 = "AY1> ";
//    public static final String CODE_EXEC_SET_BCAST_NOTIF_2 = "AY2> ";
//    public static final String CODE_SET_LOG_LEVEL_0 = "AZ0> ";
//    public static final String CODE_SHOW_SPHERE_SN_0 = "BA0> ";
//    public static final String CODE_EXEC_BROADCAST_SN_0 = "BB0> ";
//    public static final String CODE_EXEC_LOAD_SN_0 = "BC0> ";
//    public static final String CODE_SET_COMLIB_OUTBOX_0 = "BD0> ";
//    public static final String CODE_SHOW_OUTBOX_DATA_0 = "BE0> ";
//    public static final String CODE_SHOW_BLACK_USER_0 = "BF0> ";
//    public static final String CODE_EXEC_OUTBOX_RESET_0 = "BG0> ";
//    public static final String CODE_EXEC_OUTBOX_RESET_ALL_0 = "BH0> ";
//    public static final String CODE_EXEC_LOAD_MESSAGE_FILTER_0 = "BI0> ";
//    public static final String CODE_SHOW_USER_ALIAS_0 = "BJ0> ";
//    public static final String CODE_EXEC_SET_USER_ALIAS_0 = "BK0> ";
//    public static final String CODE_EXEC_REMOVE_USER_ALIAS_0 = "BL0> ";
//    
//    
//}
