NyanNyanGuard
===========
NyanNyanGuardは開発が中止されたMinMinGuardの後継版です。
ブロックする広告を増やすことを中心に更新していきます。

サポート
----------
AndroPlus Blog
http://androplus.kagome-kagome.com/

機能
----------
広告ネットワークのブロック
- Admob、Nendなどの広告ネットワークのSDKの呼び出しをブロックし、広告枠を削除します。

アプリ内広告枠の削除
- 広告ネットワークとURLのブロックだけでは消えない広告枠を個別に対応して削除します。

広告URLのブロック
- 内蔵したブロックリストに含まれるURLをブロックします。
  リストは
  MinMinGuardの元々のブロックリスト
  http://adaway.org/hosts.txt
  http://hosts-file.net/ad_servers.asp
  http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext
  http://winhelp2002.mvps.org/hosts.txt
  https://www.cubby.com/pl/hosts_Ultimate.txt/_15815deb4359441cbc9ce6b7a52c8eb0
  https://www.cubby.com/pl/hosts_for_Adaway.txt/_680e34d41b734958907ebef4380805f4
  をAdAwayで統合しています。


------------------------------------------------------------

MinMinGuard  (Project Cancelled)
===========
 [XDA discussion thread](http://forum.xda-developers.com/showthread.php?p=49112940#post49112940)  
 [XDA News - Selectively Block Ads on Certain Apps with Xposed Module](http://www.xda-developers.com/android/selectively-block-ads-on-certain-apps-with-xposed-module/)   
 [Pocketables article](http://www.pocketables.com/2014/01/minminguard-xposed-framework-module-patches-ad-holes.html)

Disclaimer
----------

* **USE MINMINGUARD AT YOUR OWN RISK**
* MinMinGuard is for experimental purposes only, and comes with no warranty or guarantee.
* Readers, end-users, and downloaders of MinMinGuard are responsible for their own actions.
* Readers, end-users, and downloaders of MinMinGuard agree not to use for illegal actions.
* This disclaimer is subject to change without notice at any time for any reason.


Index
-----

* [Description](https://github.com/chiehmin/MinMinGuard/#description)
* [Features](https://github.com/chiehmin/MinMinGuard/#features)
* [How does it work?](https://github.com/chiehmin/MinMinGuard/#how-does-it-work)
* [Frequently asked questions](https://github.com/chiehmin/MinMinGuard/#frequently-asked-questions)

Description
-----------

It can completely remove both the ads inside apps and the empty space caused by those ads. Conventional ads removing apps are only able to block the ad content, but the space taken by the ad will still remain unused (black). MinMinGuard successfully removes that black space, which extends the app window and makes your user-experience better.

Features
--------

* **Totally remove the advertisement.** You can notice that AdBlock and AdAway only stops showing the ad content, but it can not remove the empty field that was originally taken by the ad. MinMinGuard can totally remove the empty field!
* **Lightweight.** Alternative ad removing apps (AdBlock etc) constantly run a background VPN service, which puts a heavy loading on the system. MinMinGuard does not need to run a VPN service, so it saves system resources and, thus, extends the battery life.
* **Per App Setting.** MinMinGuard lets you choose which apps you want to remove ads from. If you only want to remove ads from several apps, MinMinGuard is your best solution.

How does it work?
-----------------

* **API based blocking:** Block function calls provided by the advertisement network sdk. Then recursively remove the ad space.

* **URL based blocking:** Block webviews that contains prohibited urls. 


