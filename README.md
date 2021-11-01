# 購ToEat — 全端客製化服務平台

## 目錄
- [作品簡介](#作品簡介)
- [手機前端](#手機前端)
- [網頁後端](#網頁後端)


## 作品簡介

本專題提供一套為銷售業者所設計的商品販售系統，整體採用系統通用化架構，不侷限商品販售的種類，前端控管採用Android手機應用程式，後端採用Django Web進行管理，對MySQL資料庫進行同步資料更新。
『購 to Eat』APP結合了市面上目前販售系統的優點並加以改良，給予店家與使用者雙方優質的使用體驗。業者可透過網頁服務註冊商家資訊，登錄販售商品並進行細項客製化調整。消費者可透過『購 to Eat』APP一次瀏覽多家商店，進入各店家專屬介面，選擇欲購買之商品，進行商品客製化選購。除了完善的會員制系統，還具備了商品客製化功能，消費者可針對商品細項進行調整。同理，銷售業者亦可針對商品客製化進行更新。無論是前端後端的操作，皆具備訂單瀏覽與商品評價功能，方便消費者與銷售業者進行各項控管，雙方使用者的操作(如新增、刪除商品與訂單等等)皆會同步更新至MySQL資料庫進行資料處理。

## 手機前端

APP首頁即可瀏覽目前加入「購to Eat」客製化服務平台的各個店家(如圖1-1)，點擊對應的店家即可進入該店家進行選購。

![image](https://user-images.githubusercontent.com/75149212/139483966-2dc02bb0-dff7-467d-895d-acf93f74b979.png)

###### 圖1-1  APP首頁介面 

點擊對應的店家後，即進入各店家的主介面(如圖1-2)。使用者可以瀏覽店家的最新廣告，畫面下方是飲品排行榜，顯示目前銷售量最高的前十杯飲料，點選感興趣的飲料進入選購畫面。

標題列可點選右上角按鈕即可回到APP首頁。

![image](https://user-images.githubusercontent.com/75149212/139483998-e87d123e-9f0c-4160-b17f-3dd69abd2908.png)

###### 圖1-2  APP店家首頁介面

APP瀏覽商品介面(如圖1-3)，商品依照群組進行分類。在此以手搖商家舉例，點選下方「飲料購」按鈕即轉跳至瀏覽飲品介面，依照咖啡、茶瓦納等等飲品進行細項分類。點選各類別即可查看所屬飲品，看到欲選購之飲品可直接點擊查看詳情。

![image](https://user-images.githubusercontent.com/75149212/139484025-dae2cb20-f66f-429a-8e24-1b1a9345e679.png)
![image](https://user-images.githubusercontent.com/75149212/139484034-1a979e9e-9fb1-42df-b7aa-fd3d8f559783.png)

###### 圖1-3  APP商品選單介面

APP商品選購介面(如圖1-4)，顯示單件商品之詳情與選購資訊，包括可進階客製化的選項。在此以手搖商家舉例，使用者可在飲品選購介面查看飲品詳情，調整容量、冰度、甜度、配料等等的客製化選項，並線上下單訂購飲品。

![image](https://user-images.githubusercontent.com/75149212/139484101-d4b65ec5-cd0a-4b4f-b124-17ce843d2991.png)
![image](https://user-images.githubusercontent.com/75149212/139484105-ddd82ff6-5d5b-4a0a-84cb-03983c35cbb7.png)

###### 圖1-4  APP商品選購介面

APP商品購物車介面(如圖1-5)，使用者線上下訂購買的商品將會顯示於此，點選對應品項可查看下訂資訊並選擇對應的操作。在此以手搖商家舉例，在飲品選購介面線上下訂購買後，飲品將會加入使用者的個人購物車，並在購物車介面顯示飲品資訊，點擊飲品後可查看下訂的飲品資訊，包括自行調整的客製化選項。

![image](https://user-images.githubusercontent.com/75149212/139484347-576092bb-ddc8-424e-beb0-6a8ff7f744fa.png)
![image](https://user-images.githubusercontent.com/75149212/139484359-4c15106b-e4c3-438d-ab50-eea460e6fdec.png)

###### 圖1-5  APP商品購物車介面

APP商品評價介面(如圖1-6)，顯示買家給予之該商品評價與評語，包含下訂客製化之資訊，使用者可在下訂商品前，先行瀏覽該商品的相關評價。在此以手搖商家舉例，點選飲品評價按鈕，可查看「冰美式咖啡」的飲料評分與評價，包含客製化之資訊。

![image](https://user-images.githubusercontent.com/75149212/139484409-8e926a0c-6db2-41c4-ab95-9ce83ce08088.png)
![image](https://user-images.githubusercontent.com/75149212/139484412-4d70d86c-3805-4e3d-a437-0efda70c2f45.png)

###### 圖1-6  APP商品評價介面

APP歷史訂單介面(如圖1-7)，顯示使用者曾經完成購買的歷史訂單，資訊包含下單時間與總金額。點選「訂單詳情」即可查看該筆訂單資訊。

![image](https://user-images.githubusercontent.com/75149212/139484453-286ef18a-bb95-4a48-af31-f24ce2330e89.png)

###### 圖1-7  APP歷史訂單介面

APP歷史訂單詳情介面(如圖1-8)，點選「訂單詳情」後即可查看該筆訂單的資訊，其中資訊包括訂購商品之資訊。並且使用者可在此頁對購買之商品做評分與評價動作。

![image](https://user-images.githubusercontent.com/75149212/139484484-e2d36efc-1d1d-4f68-9d08-f685e7be5f4d.png)
![image](https://user-images.githubusercontent.com/75149212/139484492-278e08d8-33ea-41fd-a497-3f96bbec3859.png)

###### 圖1-8  APP歷史訂單詳情介面

## 網頁後端

商家登入介面，輸入管理者帳號及密碼即可進入「購toEat」商家管理系統介面，進行各項管理操作(如圖2-1)。

<img src="https://user-images.githubusercontent.com/75149182/139641746-1118a6f0-8229-43e8-99f1-9baaf9aab4c6.png" width=600 height=300 />

###### 圖2-1  Web登入介面

商家註冊介面，輸入以下資料便可以創建一個店家的後台以及APP頁面(如圖2-2)。
<img src="https://user-images.githubusercontent.com/75149182/139642069-ae002a0f-dfce-4078-8bac-033f04f9f190.png" width=600 height=300 />

###### 圖2-2  Web註冊介面

管理者可在此頁瀏覽各項商品，點擊對應的商品即可瀏覽商品資訊及修改相關商品資訊(如圖2-3)。
<img src="https://user-images.githubusercontent.com/75149182/139642492-3d9df951-4c4c-4da9-8151-ec1f87ba0abb.png" width=600 height=300 />

###### 圖2-3  網頁店家商品介面

管理者可在此頁新增欲販售之新產品。填寫對應欄位並按下加入，店家資料庫便會同步新增產品(圖2-4)。

<img src="https://user-images.githubusercontent.com/75149182/139643214-2daedd6d-0031-4c88-b85f-e1760a057b8f.png" width=600 height=300 />

###### 圖2-4  網頁店家商品新增介面

網頁商品分類新增介面，管理者在此介面新增或修改分類客製化選項。在此以手搖商家舉例，管理者可在此頁新增或刪除「冰度」相關分類及可選擇之冰度細項(如圖2-5)。

<img src="https://user-images.githubusercontent.com/75149182/139643374-1ed2b35b-8258-4ce8-a966-50a7c4c24e96.png" width=600 height=300 />

###### 圖2-5  網頁商品分類新增介面

管理者在此處新增顯示於前端之廣告資訊，依照欄位填入對應資料即可新增(如圖2-6)。
<img src="https://user-images.githubusercontent.com/75149182/139643626-980dd36a-7817-4dfd-b5eb-2020bced5a45.png" width=600 height=300 />

###### 圖2-6  網頁店家廣告更新介面

管理者可預覽目前顯示於前端之廣告資訊，亦可在此頁預覽方才新增之廣告畫面(如圖2-7)。
<img src="https://user-images.githubusercontent.com/75149182/139643720-5e24ff9a-5f01-415f-a317-9cc91d8e1448.png" width=600 height=300 />

###### 圖2-7  網頁店家廣告預覽

顯示所有註冊會員之資訊，管理者可在此頁進行進一步會員管理(如圖2-8)。
<img src="https://user-images.githubusercontent.com/75149182/139643818-779acc75-2b9a-4d20-9e26-03f797e24f8c.png" width=600 height=300 />

###### 圖2-8  網頁店家會員列表

顯示未完成之訂單，管理者可透過此頁面修改用戶訂單之資訊，如用戶商品及其客製化選項(圖2-9)。

<img src="https://user-images.githubusercontent.com/75149182/139646685-f9b77dec-3dc9-4dbf-a0df-7915af2182f1.png" width=600 height=300 />

###### 圖2-9  網頁店家訂單列表(未完成)

顯示已完成訂單，可在此看詳細訂單之詳情，資訊包含會員編號與其商品客製化之選項。(如圖2-10)

<img src="https://user-images.githubusercontent.com/75149182/139647344-496d1e03-3cde-42c8-b80f-c88246d8fd39.png" width=600 height=300 />

###### 圖2-10  網頁店家已完成訂單列表

管理者可瀏覽相關訂單評價及評論，可在此頁查看訂單之資訊並進行評論操作。(如圖2-11)
<img src="https://user-images.githubusercontent.com/75149182/139647450-a00321ef-5522-460b-bc2a-4a373ce08897.png" width=600 height=300 />

###### 圖2-11  網頁店家評論列表

