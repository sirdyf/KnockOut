; Ver 5.19

Dim sdListMesh(50)
Global sdListMeshCur=0
Dim sdListNameLevel$(50)
Global sdListNameLevelCount=-1

Global sdSelectObj.sdObject=Null 
Global sdWatchObj.sdObject=Null 
Global gameEnd=0
Global sdChannel=0;
Global sdSecLevel$="Demo"

;sdSecLevel=sdSecurity()
Global sdSound1=LoadSound ("wav\ballbillard.wav")
SoundVolume sdSound1,1

Global tstSnd#=0

Include "Init.bb"
Include "Camera.bb"
Include "Light.bb"
Include "Object.bb"
Include "Scene.bb"
Include "control.bb"
Include "Editor.bb"
Include "sec.bb"

;-----------------------------------------------------Level Editor--------------
;sdEditor(view):End
;-----------------------------------------------------
sdSelectLevel()

oldTime=MilliSecs() 
While MilliSecs() < oldTime + 500
	pxRenderPhysic(30,0)
Wend 
;mTimer=CreateTimer (20)

Repeat ; Main Loop
;	WaitTimer (mTimer)
	RenderWorld()
	If  (gameEnd=0) Then
		pxRenderPhysic(30,0)
		sdObjectUpdate()
		sdProcess()	
		Text 0,60,"Difficulty - easy(luck 70% - 95%)"
		Text 0,80,"Force="+view\camForce+" Luck(" + view\lucky*100 + "%)" ;+ " _debug info: " + view\camForce*view\lucky
	Else
		sdProcessCamera()
		Text GraphicsWidth()/2.0,GraphicsHeight()/2.0," Game END!",True,True	;: Stop; + gameEnd
	EndIf 
	sdFPS()
	Text 0,40,"Bonus:" + view\bonus + " Step - " + view\steps + " tstSnd="+tstSnd
;	If (sdSelectObj<>Null )Then
;		Text 100,40, "select: " + sdSelectObj\Body
;	EndIf 
	Text 0,20,"User name: "+sdSecLevel
	Flip 1
Until KeyHit(1)
;-----------------------------------------------------

;Внимание! Функция удаления физического мира pxDestroyWorld() не удаляет магниты.
pxDestroyWorld()
End

;бонусы считаю по формуле:
; + 100 за уровень сложности (+)
; + 50 за каждую сбитую противника(+)
; - 80 за каждую сбитую свою(+)
; - 10 за каждый ход (+)
; - 30 за использование новой шашки (должно работать тока за первый удар) (+)
; -100 за выкидывание с доски бонусной фигуры(кроме шара удачи) (-)
; +бонус в конце игры за расположение скинутых черных шашек на определенной области (-)
; +бонус за более чем одну выбитую сразу шашку. (-)

;бонусные фигуры и бонусы:(давать сразу либо еще как то):
; зеленые - оч.тяжелые (+)
; красные - притягивающий магнит(+)
; синие - отталкивающий магнит(+)
; желтые - шары удачи(если заденешь +/- удача 20%, по идее шар потом укатиться должен) (-)
; фиолетовые: +/- бонус за попадание(потом исчезает) (-)
; движущиеся зел.шашки - движутся между двумя заданными положениями (+)
; невидимая(?) - замораживает на 3 хода (своя становиться зеленной)
; движущийся магниты
; желтая = 1/2 зеленой
; разрушающиеся шашки ( пропадает от двух попаданий)
; своя шашка - бомба
; превращение шашки противника в свою
; воздушные потоки(вверх)
; бегущие дорожки (на плоскости)
; своя тяжелая шашка (не забыть увеличить размеры и силу, чтобы ей можно было рулить как обычной) - танк он и в Африке танк :)
; своя антимагнит - на нее не действют магниты
;магнитнолевитационная ловушка - при попадании шашка зависает в воздухе.
; телепорт
; ...
; Игровые элементы:
;
; Для доработки:
; бонус и удача задаются в нескольких местах - сделать в одном
; анимация - ?
;
;Text 0,150,"phisXobj="+px_num+" sdObj="+sdObj_num+" sdWhite="+sdWhite_num+" sdBlack="+sdBlack_num+" sdNeutral="+sdNeutral_num
; Table - is too sdObj !!!
;pxCreateWorld(1, "key")	- added one pxObect - plane !!!

; При нажатии на колесико - рестарт уровня
; 	;Stop
	;sdCreateNeutral()
	;sdCreateMagnet()
	;sdCreateMoved()
;	tmpObj.sdNeutral = First sdNeutral
;	tmpObj = After tmpObj
;	tmpObj.sdMoved= First sdMoved
