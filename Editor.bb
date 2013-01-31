; Ver 5.10
;-----------------------------------------------------Level Editor--------------
; сделать вкл/выкл физики
; корректно удалять магниты при создании/удалении, загрузки/выгрузки уровня

Global sdSelectMesh
Function sdEditor(ent.sdCamera)
	sdCreateTable()
	sdCreateWhite()
	sdCreateBlack()
	sdCreateNeutral()
	sdCreateMagnet()
	sdCreateMoved()
	sdSetAllPickable()
	pxRenderPhysic(30,0)
	sdObjectUpdate()
	
Repeat ; Main Loop
	RenderWorld()
	sdProcessClickEditMode(ent)
	sdProcessCamera()
	sdProcessKeyEditor()
	If (sdSelectMesh) Then
		dx#=MouseXSpeed()*0.05
		dy#=MouseYSpeed()*0.05
		MoveEntity (sdSelectMesh,dx,0,-dy)
		; ----------For Moved saska:--->
		For obj.sdMoved = Each sdMoved
			If obj\saska\Mesh=sdSelectMesh Then
				MoveEntity (obj\firstMesh,dx,0,-dy)
				MoveEntity (obj\secondMesh,dx,0,-dy)
			EndIf
			If obj\firstMesh=sdSelectMesh Then sdMovedPolus(obj)
			If obj\secondMesh=sdSelectMesh Then sdMovedPolus(obj)
		Next 
		; ---------------<
	EndIf
	sdFPS()

	;Text 0,40,"CamX="+(MouseX()-1027/2.0)/50.0 +" CamY="+(MouseY()-768/2.0)/50.0
	;Text 0,20,"Zoom="+view\camZoom+" CamX="+MouseXSpeed() +" CamY="+MouseYSpeed()+" CamZ="+MouseZSpeed()
	Text GraphicsWidth()/2.0,GraphicsHeight()-30,"(c)SDE_Group(Chapaev v5.18 Demo). mail: elite-2k8@land.ru",True
	;Text 0,20,"Selected=" + sdSelectMesh
MouseXSpeed() : MouseYSpeed()
	Text 10,50,"1 - delete Selected(!) mesh"
	Text 10,70,"2 - add White saska"
	Text 10,90,"3 - add Black saska"
	Text 10,110,"4 - add Bonus saska"
	Text 10,130,"5 - centered All"
	Text 10,145,"6 - add Moved saska"
	Text 10,160,"7 - add (+)Magnet"
	Text 10,175,"8 - add (-)Magnet"

	Text GraphicsWidth()/2,5,"F1/F2/F3 - switch camera view, SPACE - drop cur/saska, ESC - Exit and Save",True,False
	Text  GraphicsWidth()/2,20,"Press well - free rotate, scroll weel - zooming",True,False
	Text GraphicsWidth()/2,35,"LMB - select/drop, RMB - 1/2 centered",True,False
	Text GraphicsWidth()/2,50,"p - enable/disable physics",True,False
	Text GraphicsWidth()/2,65,"+/- up/down",True,False
	Flip 1

Until KeyHit(1)
	sdSaveLevel()
End Function

Function sdProcessKeyEditor()
	If KeyHit (2) Then
		If (sdSelectMesh) Then
			sdEditorDeleteCurrentSaska()
		EndIf 
	EndIf 
	If KeyHit (3) sdCreateWhiteSaska(0,10,0) ;
	If KeyHit (4) sdCreateBlackSaska(0,10,0) : sdSetAllPickable()
	If KeyHit (5) sdCreateNeutralSaska(0,10,0) : sdSetAllPickable()
	If KeyHit (6) sdCenteredAll()
	If KeyHit (7) sdCreateMovedSaska(0,10,0,10,10,0,-10,10,0) : sdSetAllPickable()
	If KeyHit (8) sdCreateMagnetMesh(0,10,0,1)
	If KeyHit (9) sdCreateMagnetMesh(0,10,0,-1)
	
	If KeyHit (59) sdResetCameraCenter()
	If KeyHit (60) sdResetCamera()
	If KeyHit (61) sdResetCameraDown()
	
	If KeyHit (57) Then ;" "
		If (sdSelectMesh) Then
			cPos.Vector=Vector()
			Vector_Set_Entity_Pos(sdSelectMesh,cPos)
			cPos\y=4.3
			Vector_PositionEntity (sdSelectMesh,cPos)
			; ----------For Moved saska:--->
			For obj.sdMoved = Each sdMoved
				If obj\saska\Mesh=sdSelectMesh Then
					Vector_Set_Entity_Pos(obj\firstMesh,cPos)
					cPos\y=4.3
					Vector_PositionEntity (obj\firstMesh,cPos)
					Vector_Set_Entity_Pos(obj\secondMesh,cPos)
					cPos\y=4.3
					Vector_PositionEntity (obj\secondMesh,cPos)
				EndIf
			Next 
			; ---------------<
		EndIf 
	EndIf
	
	If KeyHit (78) Then sdEditLevel(0.805)
	If KeyHit (74) Then sdEditLevel(-0.805)
End Function

Function sdEditLevel(lev#)
	If (sdSelectMesh) Then 
		cPos.Vector=Vector()
		Vector_Set_Entity_Pos(sdSelectMesh,cPos)
		cPos\y=cPos\y + lev
		Vector_PositionEntity (sdSelectMesh,cPos)
		; ----------For Moved saska:--->
		For obj.sdMoved = Each sdMoved
			If obj\saska\Mesh=sdSelectMesh Then
				Vector_Set_Entity_Pos(obj\firstMesh,cPos)
				cPos\y=cPos\y + lev
				Vector_PositionEntity (obj\firstMesh,cPos)
				Vector_Set_Entity_Pos(obj\secondMesh,cPos)
				cPos\y=cPos\y + lev
				Vector_PositionEntity (obj\secondMesh,cPos)
			EndIf
		Next 
		; ---------------<
	EndIf 
End Function 

Function sdEditorDeleteCurrentSaska()
	If (sdSelectMesh) Then
		For obj1.sdWhite = Each sdWhite
			If obj1\saska\Mesh=sdSelectMesh Then
				Delete obj1
			EndIf
		 Next
		For obj2.sdBlack = Each sdBlack
			If obj2\saska\Mesh=sdSelectMesh Then
				Delete obj2
			EndIf 
		 Next
		For obj3.sdNeutral = Each sdNeutral
			If obj3\saska\Mesh=sdSelectMesh Then
				Delete obj3
			EndIf 
		 Next
		For obj4.sdMoved = Each sdMoved
			If obj4\saska\Mesh=sdSelectMesh Then
				FreeEntity (obj4\firstMesh)
				FreeEntity (obj4\secondMesh)
				Vector_Free(obj4\firstPos)
				Vector_Free(obj4\secondPos)
				Delete obj4
			EndIf 
		 Next
		For obj5.sdMagnet = Each sdMagnet
			If obj5\Mesh=sdSelectMesh Then
				FreeEntity (obj5\Mesh)
				pxMagnetDelete(obj5\Magnet)
				Delete obj5
			EndIf 
		 Next
		For obj.sdObject = Each sdObject
			If obj\Mesh=sdSelectMesh Then
				pxDeleteBody(obj\Body)
				FreeEntity(sdSelectMesh)
				Delete obj
			EndIf 
		Next
		sdSelectMesh=0
	EndIf 
End Function

Function sdCenteredAll()
	i=0
		For obj.sdObject = Each sdObject
			If i<>0 Then
				tx#=EntityX(obj\Mesh)
				ty#=EntityY(obj\Mesh)
				tz#=EntityZ(obj\Mesh)
				;sdCreateWhiteSaska((i-4.5)*2.8, 0.3 +4, -10 + (i Mod 2)*3)
				dx#=Floor(tx/2.8)
				;s%=Sgn (tz)
				dz#=Ceil(tz/2.8);*2.8-1.4
				;dz#=Floor(tz/2.8)
				PositionEntity (obj\Mesh,dx*2.8+1.4,ty,dz*2.8-1.4)
				;Stop
			EndIf 
			i=i+1
		Next
End Function 

Function sdCenteredHalf(tmesh)
		For obj.sdObject = Each sdObject
			If obj\Mesh=tmesh Then
				tx#=EntityX(obj\Mesh)
				ty#=EntityY(obj\Mesh)
				tz#=EntityZ(obj\Mesh)
				dx#=Floor(tx/1.4)
				dz#=Ceil(tz/1.4)
				PositionEntity (obj\Mesh,(dx)*1.4,ty,dz*1.4-1.4)
			EndIf 
		Next
End Function 

Function sdSetAllPickable()
	For obj.sdObject = Each sdObject
		EntityPickMode obj\Mesh, 2
	Next
	obj=First sdObject
	EntityPickMode obj\Mesh, 0 ; skip table
	
;	i=0
;	For obj1.sdWhite = Each sdWhite
;		EntityPickMode obj1\saska\Mesh, 2
;		i=i+1
;	Next
;	i=1
;	For obj2.sdBlack = Each sdBlack
;		EntityPickMode obj2\saska\Mesh, 2
;		i=i+1
;	Next
;	i=1
;	For obj3.sdNeutral = Each sdNeutral
;		EntityPickMode obj3\saska\Mesh,2
;		i=i+1
;	Next
;	For obj4.sdMagnet = Each sdMagnet
;		EntityPickMode obj4\Mesh,2
;	Next
End Function

Function sdProcessClickEditMode(ent.sdCamera)

	If MouseHit(1) = True Then
		;HidePointer()
		If (sdSelectMesh ) Then
			sdSelectMesh = 0
			Return
		EndIf 
		
		CameraPick(ent\camera, MouseX(), MouseY())
		sdSelectMesh = PickedEntity()
		;sdSelectObj=Null
		;For obj.sdObject = Each sdObject
			;If obj\Mesh=SelectMesh Then sdSelectObj=obj
		;Next 
;		For obj.sdWhite = Each sdWhite
;			If obj\saska\Mesh = SelectMesh Then
;				sdSelectObj = obj\saska
;			EndIf 
;		Next
;		For obj2.sdBlack = Each sdBlack
;			If obj2\saska\Mesh = SelectMesh Then
;				sdSelectObj = obj2\saska
;			EndIf 
;		Next
;		For obj3.sdNeutral = Each sdNeutral
;			If obj3\saska\Mesh = SelectMesh Then
;				sdSelectObj = obj3\saska
;			EndIf 
;		Next
	EndIf
	If MouseHit(2) = True Then ;sdSelectObj=Null
		CameraPick(ent\camera, MouseX(), MouseY())
		SelectMesh = PickedEntity()
		sdCenteredHalf(SelectMesh)
	EndIf
	If MouseDown (3) Then
		sdMouseLookTarget()
	EndIf
	zoom#=MouseZSpeed()
	If (zoom<>0) Then
		view\camZoom=view\camZoom+zoom
		sdMouseLookTarget()
	EndIf 
End Function

Function sdMovedPolus(ent.sdMoved)
	nPosX#=(EntityX (ent\firstMesh)+EntityX (ent\secondMesh))/2.0
	nPosY#=(EntityY (ent\firstMesh)+EntityY (ent\secondMesh))/2.0
	nPosZ#=(EntityZ (ent\firstMesh)+EntityZ (ent\secondMesh))/2.0
	PositionEntity (ent\saska\Mesh,nPosX,nPosY,nPosZ)
End Function 
