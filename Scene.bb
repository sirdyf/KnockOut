;Ver 5.19

HWMultiTex enable

CameraClsColor view\Camera, 50,50,50

Function sdCreateTable()
	sdMaterialMeshDefault = CreateBrush()

	BrushColor sdMaterialMeshDefault, 150, 150, 150
	BrushAlpha sdMaterialMeshDefault, 1
	BrushShininess sdMaterialMeshDefault, 1

	sdMaterialBodyDefault = pxCreateMaterial()
	pxMaterialSetDyFriction(sdMaterialBodyDefault, 0.25)
	pxMaterialSetStFriction(sdMaterialBodyDefault,  0.1) 	
	pxMaterialSetRestitution(sdMaterialBodyDefault,0.85)
	
	;Plane
	;sdPlane=CreatePlane()
	;sdTexure = LoadTexture("tile.jpg")
	;EntityTexture sdPlane, sdTexure
	;EntityAlpha sdPlane, 0.9
	
	sdTexure = LoadTexture("chessboard.png")
	sdTable.sdObject = sdCreateBox(0, -0.001+4, 0, 13, 0.001, 13, 0)
	pxBodySetFlagContacttable(sdTable\Body,0)
	
	EntityTexture sdTable.sdObject\Mesh, sdTexure
	;CreateMirror(); sdTable.sdObject\Mesh
	EntityAlpha sdTable.sdObject\Mesh, 0.7
	
	;sdShadowSpr=LoadSprite("Particle-004.png")
	
	;PositionEntity sdShadowSpr,0,5,0
	;SetBuffer TextureBuffer(sdTexure)
	;Oval 0,0,512,512
	;SetBuffer BackBuffer()
	
	;HideBox = CreateCube()
	;ScaleEntity HideBox, 14, 1, 14
	;PositionEntity HideBox, 0, 5, 0
End Function 

Function sdCreateSaska.sdObject(PosX#=0, PosY#=0, PosZ#=0, Radius#=1, Height#=1, Segment=16, Mass#=1, MaterialMesh=0, MaterialBody=0)

	obj.sdObject		= New sdObject
	obj\Mesh      		= LoadMesh("saska.3DS")
	ScaleEntity obj\Mesh, Radius, Height * 3, Radius

	If MaterialMesh Then
		PaintEntity obj\Mesh, MaterialMesh
	Else
		PaintEntity obj\Mesh, sdMaterialMeshDefault
	EndIf
	
	obj\Body = pxBodyCreateCylinder(Radius, Height, Segment, Mass)

	If MaterialBody Then
		pxMaterialSetToBody(obj\Body, MaterialBody)
	Else
		pxMaterialSetToBody(obj\Body, sdMaterialBodyDefault)
	EndIf
	
	pxBodySetPosition obj\Body, PosX, PosY, PosZ
	pxBodySetEntity obj\Mesh, obj\body

	obj\frost=0
	obj\used=0
	obj\drop=0
	obj\colBodyTime=0
	obj\colBody=0
	Return obj
End Function

Function sdCreateWhite()
	For i=1 To 8
		sdCreateWhiteSaska((i-4.5)*2.8, 0.3 +4, -10 + (i Mod 2)*3)
	Next
End Function
Function sdCreateWhiteSaska(px#,py#,pz#)
	obj.sdWhite 	= New sdWhite
	obj\saska    	= sdCreateSaska(px,py,pz, 1, 0.4)
	EntityColor obj\saska\Mesh,150,150,150
	EntityPickMode obj\saska\Mesh, 2
End Function 

Function sdCreateBlack()
	For i=1 To 8
		sdCreateBlackSaska((i-4.5)*2.8, 0.3+4, 10- (i Mod 2)*3)
	Next
End Function
Function sdCreateBlackSaska(px#,py#,pz#)
	obj.sdBlack	= New sdBlack
	obj\saska		= sdCreateSaska(px,py,pz, 1, 0.4)
	EntityColor obj\saska\Mesh, 30, 30, 30
End Function 

Function sdCreateNeutral()
	sdCreateNeutralSaska((3-4.5)*2.8+2.8/2.0, 0.3+4, 0)

	sdCreateNeutralSaska((5-4.5)*2.8+2.8/2.0, 0.3+4, 0)
End Function
Function sdCreateNeutralSaska(px#,py#,pz#)
	obj.sdNeutral	= New sdNeutral
	obj\saska		= sdCreateSaska(px,py,pz,1, 0.4, 16,10)
	EntityColor obj\saska\Mesh, 0,50,0
End Function

Function sdCreateMagnet()
	sdCreateMagnetMesh((1-4.5)*2.8+2.8/2.0, 0.3+4, 0,1)
	sdCreateMagnetMesh((7-4.5)*2.8+2.8/2.0, 0.3+4, 0,-1)
	
;	m.sdMagnet = New sdMagnet
;	m\Mesh=CreateCube ()
;	m\Magnet=pxCreateMagnet(-1,-5,-10)
;	PositionEntity m\Mesh,(7-4.5)*2.8+2.8/2.0, 0.3+4, 0
;	ScaleEntity m\Mesh,0.3,1,0.3
;	EntityColor m\Mesh, 0,0,50
;	EntityPickMode m\Mesh, 2
;	pxMagnetSetPosition(m\Magnet,(7-4.5)*2.8+2.8/2.0, 0.1+4, 0)  ; устанавливаем позицию
;	pxMagnetSetMaxRadius(m\Magnet, 3)    ; устанавливаем макс радиус действи€
;End Function
End Function
Function sdCreateMagnetMesh(px#,py#,pz#,znak%)
	If znak <>0 Then 
		m.sdMagnet = New sdMagnet
		m\Mesh=CreateCube()
		m\Magnet=pxCreateMagnet(znak*3,znak*5,znak*7)
		If znak>0 Then ; Ёта функци€ создаЄт магнит прит€гивающий - красный
			ScaleEntity m\Mesh,0.3,1,0.3
			EntityColor m\Mesh, 50,0,0
		Else ; Ёта функци€ создаЄт магнит отталкивающий - синий
			ScaleEntity m\Mesh,1,0.3,0.3
			EntityColor m\Mesh, 0,0,50
		EndIf 
		PositionEntity m\Mesh,px,py,pz
		EntityPickMode m\Mesh, 2
		pxMagnetSetPosition(m\Magnet,px,py,pz)  ; устанавливаем позицию
		pxMagnetSetMaxRadius(m\Magnet, 3)    ; устанавливаем макс радиус действи€
		;pxMagnetSetMinRadius(m\Magnet,2)	; устанавливаем мин радиус действи€
	EndIf 
End Function 

Function sdCreateMoved()
	sdCreateMovedSaska(0,4.3,-4,10,4.3,-4,-10,4.3,-4)
End Function
Function sdCreateMovedSaska(px#,py#,pz#,fx#,fy#,fz#,sx#,sy#,sz#)
;mat=pxCreateMaterial()
;pxMaterialSetDyFriction(mat, 0.1)
;fric=[0,inf]дл€ динамических тел
;pxMaterialSetStFriction(mat, 0.1)
;fric=[0,inf]дл€ статических тел
;pxMaterialSetRestitution(mat,0.2)	
;rest=[0,1]дл€ любых тел
	obj.sdMoved	= New sdMoved
	obj\saska		= sdCreateSaska(px,py,pz,1, 0.4, 16,5)
	EntityColor obj\saska\Mesh, 0,50,0
	obj\saska\used=0:	obj\saska\drop=0
	obj\firstPos= Vector(fx,fy,fz)
	obj\secondPos=Vector(sx,sy,sz)
	obj\force=0.2

	obj\firstMesh=CreateCube()
	Vector_PositionEntity(obj\firstMesh,obj\firstPos)
	ScaleEntity obj\firstMesh,0.3,1,0.3
	EntityColor obj\firstMesh, 0,50,50
	EntityPickMode obj\firstMesh, 2

	obj\secondMesh=CreateCube()
	Vector_PositionEntity(obj\secondMesh,obj\secondPos)
	ScaleEntity obj\secondMesh,0.3,1,0.3
	EntityColor obj\secondMesh, 0,50,50
	EntityPickMode obj\secondMesh, 2
End Function


Function sdPrintInfoAboutBonus()
	Text 100 ,120,"-“€желый"
	Text 100 ,140,"-ћагнит"
	Text 100 ,160,"-ћагнит"
	Text 100 ,180,"-Ѕонус шар"
End Function

Function sdCheckEndAndBonus(ent.sdCamera)
	endb=0
	
	For obj.sdBlack = Each sdBlack; провер€ем черные
		posY#=EntityY (obj\saska\mesh)
		If (posY<4) Then
			If obj\saska\drop=0 Then
				obj\saska\drop=1
				ent\bonus=ent\bonus+50;бонусы
			EndIf
		Else
			endb=1
		EndIf 
	Next
	
	If endb=0 gameEnd=1; все черные сброшены
	
	endw=0
	
	For obj2.sdWhite = Each sdWhite ;провер€ем белые
		posY#=EntityY (obj2\saska\mesh)
		If (posY<4) Then
			If obj2\saska\drop=0 Then
				obj2\saska\drop=1
				ent\bonus=ent\bonus-80;бонусы
			EndIf
		Else
			endw=1
		EndIf 
	Next
;Stop 
	coll=0
	For objAll.sdObject = Each sdObject
		posY#=EntityY (objAll\mesh)
		If (posY>3.9) Then
			cl%=pxGetContacts(objAll\Body)
			If (cl>1) Then
				tmpBody=pxContactGetBody(objAll\Body,cl-1) ; получили участника столкновени€
				objAll\colBodyTime=objAll\colBodyTime+1
				objAll\colBody=tmpBody
			Else
				If (objAll\colBody<>0) Then
					If (objAll\colBodyTime<3) Then
						DebugLog objAll\colBodyTime
						If (pxBodyGetLinearSpeed(objAll\Body)>0.9)	coll=1
					EndIf 
				objAll\colBody=0
				objAll\colBodyTime=0
				EndIf 
			EndIf
		EndIf
	Next
	If (coll>0) Then
		sdChannel=PlaySound (sdSound1)
	EndIf
	
	If endw=0 gameEnd=2; все белые сброшены

	If (gameEnd<>0) Then
		sdResetCameraCenter()
	EndIf 
End Function

Function sdStep()
	ww=0
	For obj2.sdWhite = Each sdWhite ;провер€ем белые
		If (obj2\saska\frost>0) Then ; если есть замороженные
			obj2\saska\frost=obj2\saska\frost-1
			If (obj2\saska\frost=0) Then ; unfrosen
				pxBodySetMass(obj2\saska\Body,1)
				EntityColor obj2\saska\Mesh, 150,150,150
			EndIf 
		EndIf
		posY#=EntityY (obj2\saska\mesh)
		If (posY>3.9) ww=ww+1
	Next
	view\white_count=ww
	
	view\steps=view\steps+1
End Function

Function sdProcessMagnet()
;» в цикле активируем магнит:
;pxMagnetActivate(my_magnet,0,0)
;где:
;mdata% - магнит
;mmode% - тип магнита
;fmode% - тип силы
;
;mmode - тип магнита.
;1 - магнит не имеющий области остановки 
;2 - с областью остановки
;3 - тела прит€гиваютс€ не по силе, а по скоросте (формула дл€ скорости аналогична формуле по силе) 
;fmode - тип силы (как в pxBodyAddForce и подобных)
	For obj.sdMagnet = Each sdMagnet
		pxMagnetActivate(obj\Magnet,0,0)
	Next 
End Function 

Function sdProcessMoved()
	For obj.sdMoved=Each sdMoved
		;obj.sdMoved=First sdMoved
		;Stop
		curPos.Vector=Vector()
		tmpV.Vector=Vector()
		;Stop
		Vector_Set_Entity_Pos(obj\saska\Mesh,curPos)
		Vector_Subtract(tmpV,obj\firstPos,curPos)
		leng#=Vector_Magnitude(tmpV)
		;Stop
		If (leng<1.1) Then
			;Stop
			Vector_Swap(obj\secondPos,obj\firstPos)
			;pxBodySetPosition(obj\saska\Body,obj\secondPos\x,obj\secondPos\y,obj\secondPos\z)
		Else
		;Stop
			;If curPos\y<4.5 Then
			;	If tmpV\y<0 Then tmpV\y=0.001
			;EndIf
			tmpV\y=0
			Vector_Normalize(tmpV)
			tmpV\y=0.5
			Vector_MultiplyScalar(tmpV,tmpV,0.2);obj\force)
			
			pxBodyAddForce(obj\saska\Body,tmpV\x,tmpV\y,tmpV\z,2);0);2)
		EndIf
	Next
End Function

Function sdProcessWatch()
	If (sdWatchObj<>Null ) Then ; Frost
	;	If (pxBodyGetLinearSpeed(sdWatchObj\Body)<0.5) Then
		If (view\status=0) Then
			If (pxBodyGetLinearMomentum(sdWatchObj\Body)<0.5) Then
					If (view\white_count>3) Then
						pxBodySetMass(sdWatchObj\Body,10)
						EntityColor sdWatchObj\Mesh, 0,100,0
						sdWatchObj\frost=3
						sdWatchObj=Null
					EndIf 
			EndIf
		EndIf 
	EndIf 
	; ƒоделать:
	; 1 - если на доске остались только замороженые - освободить одну (+)
	; 2 - если белой шашкой на прошлом ходу были сброшены 2 черные - не замораживать ее
	; 3 - можно ли выбирать сброшенную белую ?
End Function 

Function sdProcess()
	;обща€ ф-ци€ дл€ всех Process

	sdProcessCamera()
	sdProcessClickMouse(view)

	sdCheckEndAndBonus(view)

	sdProcessMagnet()
	sdProcessMoved()
	sdProcessWatch()
	;sdPrintInfoAboutBonus()
End Function 

Function sdResetLevel(ent2.sdCamera)
	sdResetCamera()
	ent2\bonus=100
	i=1
	For obj1.sdBlack = Each sdBlack
		pxBodySetPosition obj1\saska\Body,(i-4.5)*2.8, 0.3+4, 10- (i Mod 2)*3
		pxBodySetRotation obj1\saska\Body,0,0,0
		EntityPickMode obj1\saska\Mesh, 0
		obj1\saska\used=0
		obj1\saska\drop=0
		i=i+1
	Next
	i=1
	For obj2.sdWhite = Each sdWhite
		pxBodySetPosition obj2\saska\Body,(i-4.5)*2.8, 0.3 +4, -10 + (i Mod 2)*3
		pxBodySetRotation obj2\saska\Body,0,0,0
		EntityPickMode obj2\saska\Mesh, 2
		obj2\saska\used=0
		obj2\saska\drop=0
		i=i+1
	Next
	i=1
	For obj3.sdNeutral = Each sdNeutral
		If i=1 Then pxBodySetPosition obj3\saska\Body,(3-4.5)*2.8+2.8/2.0, 0.3+4, 0
		If i=2 Then pxBodySetPosition obj3\saska\Body,(5-4.5)*2.8+2.8/2.0, 0.3+4, 0
		EntityPickMode obj3\saska\Mesh,0
		obj3\saska\used=0
		obj3\saska\drop=0
		i=i+1
	Next
;	For obj4.sdMagnet = Each sdMagnet
End Function 

Function sdSaveLevel()
	; Open a file to write to 
	fileout = WriteFile("mydata.dat") 
	WriteLine (fileout,"SDC5_5")
	i=0
	For obj1.sdWhite = Each sdWhite
		WriteInt (fileout,1)
		WriteFloat( fileout, EntityX(obj1\saska\Mesh) )
		WriteFloat( fileout, EntityY(obj1\saska\Mesh) )
		WriteFloat( fileout, EntityZ(obj1\saska\Mesh) )
		i=i+1
	Next
	i=1
	For obj2.sdBlack = Each sdBlack
		WriteInt (fileout,2)
		WriteFloat( fileout,EntityX(obj2\saska\Mesh))
		WriteFloat( fileout, EntityY(obj2\saska\Mesh) )
		WriteFloat( fileout, EntityZ(obj2\saska\Mesh) )
		i=i+1
	Next
	i=1
	For obj3.sdNeutral = Each sdNeutral
		WriteInt (fileout,3)
		WriteFloat( fileout, EntityX(obj3\saska\Mesh) )
		WriteFloat( fileout, EntityY(obj3\saska\Mesh) )
		WriteFloat( fileout, EntityZ(obj3\saska\Mesh) )
		i=i+1
	Next
	For obj4.sdMagnet = Each sdMagnet
		If (pxMagnetGetMidForce(obj4\Magnet) > 0 ) Then
			WriteInt (fileout,4)
		Else 
			WriteInt (fileout,5)
		EndIf 
		WriteFloat( fileout, EntityX(obj4\Mesh) )
		WriteFloat( fileout, EntityY(obj4\Mesh) )
		WriteFloat( fileout, EntityZ(obj4\Mesh) )
		i=i+1
	Next
	For obj5.sdMoved = Each sdMoved
		WriteInt (fileout,6)
		WriteFloat( fileout, EntityX(obj5\saska\Mesh) )
		WriteFloat( fileout, EntityY(obj5\saska\Mesh) )
		WriteFloat( fileout, EntityZ(obj5\saska\Mesh) )
		i=i+1
		WriteInt (fileout,7)
		WriteFloat( fileout, EntityX(obj5\firstMesh) )
		WriteFloat( fileout, EntityY(obj5\firstMesh) )
		WriteFloat( fileout, EntityZ(obj5\firstMesh) )
		i=i+1
		WriteInt (fileout,7)
		WriteFloat( fileout, EntityX(obj5\secondMesh) )
		WriteFloat( fileout, EntityY(obj5\secondMesh) )
		WriteFloat( fileout, EntityZ(obj5\secondMesh) )
		i=i+1
	Next
	; Close the file 
	CloseFile( fileout )
End Function

Function sdDeleteAll()
	For obj1.sdWhite = Each sdWhite
		Delete obj1
	 Next
	For obj2.sdBlack = Each sdBlack
		Delete obj2
	 Next
	For obj3.sdNeutral = Each sdNeutral
		Delete obj3
	 Next
	For obj4.sdMoved = Each sdMoved
		FreeEntity (obj4\firstMesh)
		FreeEntity (obj4\secondMesh)
		Vector_Free(obj4\firstPos)
		Vector_Free(obj4\secondPos)
		Delete obj4
	 Next
	For obj5.sdMagnet = Each sdMagnet
		FreeEntity (obj5\Mesh)
		pxMagnetDelete(obj5\Magnet)
		Delete obj5
	 Next
	For obj.sdObject = Each sdObject
		pxDeleteBody(obj\Body)
		FreeEntity(obj\Mesh)
		Delete obj
	Next
End Function

Function sdLoadLevel(name$)
; PhysicX
;pxDestroyWorld()

;pxCreateWorld(1, "key")
;pxSetGravity(0,-10,0)		
;pxSDKSetParameter(0.01) ; √лубина проникновени€ 0.05 default
	gameEnd=0
	view\steps=0
	view\bonus=100
	;ClearWorld (True,True ,True ) ; уничтожает доску и камеру!
	;ClearWorld (False,True ,True )
	sdDeleteAll()

	sdCreateTable() ; 

	; Open a file to write to 
	fileout = ReadFile(name) 
	ver$=ReadLine (fileout)
	i=0
While Not Eof(fileout)
 ;	For obj1.sdWhite = Each sdWhite
		t=ReadInt (fileout)
		pX#=ReadFloat( fileout)
		pY#=ReadFloat( fileout)
		pZ#=ReadFloat( fileout)
		If t=1 sdCreateWhiteSaska(pX,pY,pZ)
		If t=2 sdCreateBlackSaska(pX,pY,pZ) 
		If t=3 sdCreateNeutralSaska(pX,pY,pZ)
		If t=4 sdCreateMagnetMesh(pX,pY,pZ,1)
		If t=5 sdCreateMagnetMesh(pX,pY,pZ,-1)
		If (t=6) Then
			t=ReadInt (fileout)
			If t<>7 Stop 
			pX1#=ReadFloat( fileout)
			pY1#=ReadFloat( fileout)
			pZ1#=ReadFloat( fileout)
			t=ReadInt (fileout)
			If t<>7 Stop 
			pX2#=ReadFloat( fileout)
			pY2#=ReadFloat( fileout)
			pZ2#=ReadFloat( fileout)
			tmpM=sdCreateMovedSaska(pX,pY,pZ,pX1,pY1,pZ1,pX2,pY2,pZ2)
		EndIf 
		i=i+1
;	Next
Wend
;	For obj4.sdMagnet = Each sdMagnet

	; Close the file 
	CloseFile( fileout )
	px_num=pxGetNumberAllBody()
	sdObj_num=0;
    For obj.sdObject = Each sdObject
		sdObj_num=sdObj_num+1
    Next
	sdWhite_num=0
    For obj1.sdWhite = Each sdWhite
		sdWhite_num=sdWhite_num+1
	 Next
	 sdBlack_num=0
    For obj2.sdBlack = Each sdBlack
		sdBlack_num=sdBlack_num+1
	 Next
	 sdNeutral_num=0
    For obj3.sdNeutral = Each sdNeutral
		sdNeutral_num=sdNeutral_num+1
	 Next
	;Stop
	;«аменить создание объектов на копирование
End Function
Function sdLoadLevelPreview(name$)
	For i=0 To 49
		If (sdListMesh(i)<>0) Then
			FreeEntity sdListMesh(i)
			sdListMesh(i)=0
		EndIf 
	Next
	; Open a file to write to 
	fileout = ReadFile(name)
	If fileout=0 Return 
	ver$=ReadLine (fileout)
	testMesh      		= LoadMesh("saska.3DS")
	PositionEntity testMesh,0,0,0
	ScaleEntity testMesh, 1, 0.4 * 3, 1
	i=0
	
While Not Eof(fileout)
		t=ReadInt (fileout)
		pX#=ReadFloat( fileout)
		pY#=ReadFloat( fileout)
		pZ#=ReadFloat( fileout)
		If (t=1) Then
			sdListMesh(i) = CopyEntity (testMesh)
			EntityColor sdListMesh(i),150,150,150
			PositionEntity sdListMesh(i),pX,pY,pZ
		EndIf 
		If t=2 Then 
			sdListMesh(i) = CopyEntity (testMesh)
			EntityColor sdListMesh(i),30,30,30
			PositionEntity sdListMesh(i),pX,pY,pZ
		EndIf 
		If t=3 Then
			sdListMesh(i) = CopyEntity (testMesh)
			EntityColor sdListMesh(i),0,50,0
			PositionEntity sdListMesh(i),pX,pY,pZ
		EndIf 
		If t=4 Then
			sdListMesh(i) = CreateCube ()
			ScaleEntity sdListMesh(i),0.3,1,0.3
			EntityColor sdListMesh(i), 50,0,0
			PositionEntity sdListMesh(i),pX,pY,pZ
		EndIf 
		If t=5 Then
			sdListMesh(i) = CreateCube ()
			ScaleEntity sdListMesh(i),1,0.3,0.3
			EntityColor sdListMesh(i), 0,0,50
			PositionEntity sdListMesh(i),pX,pY,pZ
		EndIf 
		If (t=6) Then
			t=ReadInt (fileout)
			If t<>7 Stop 
			pX1#=ReadFloat( fileout)
			pY1#=ReadFloat( fileout)
			pZ1#=ReadFloat( fileout)
			t=ReadInt (fileout)
			If t<>7 Stop 
			pX2#=ReadFloat( fileout)
			pY2#=ReadFloat( fileout)
			pZ2#=ReadFloat( fileout)
			;tmpM=sdCreateMovedSaska(pX,pY,pZ,pX1,pY1,pZ1,pX2,pY2,pZ2)
			sdListMesh(i) = CopyEntity (testMesh)
			EntityColor sdListMesh(i),0,50,0
			PositionEntity sdListMesh(i),pX,pY,pZ
			sdListMesh(i+1)=CreateCube()
			PositionEntity sdListMesh(i+1),pX1,pY1,pZ1
			ScaleEntity sdListMesh(i+1),0.3,1,0.3
			EntityColor sdListMesh(i+1), 0,50,50
			sdListMesh(i+2)=CreateCube()
			PositionEntity sdListMesh(i+2),pX2,pY2,pZ2
			ScaleEntity sdListMesh(i+2),0.3,1,0.3
			EntityColor sdListMesh(i+2), 0,50,50
			i=i+2
		EndIf
		i=i+1
		If i>50 Exit 
Wend
;	For obj4.sdMagnet = Each sdMagnet

	; Close the file 
	CloseFile( fileout )
	FreeEntity testMesh
End Function

Function sdSelectLevel()
	Local texture = LoadTexture("chessboard.png")
	table = CreateCube()
	PositionEntity table, 0, -0.001+4, 0
	ScaleEntity table,13, 0.001, 13
	EntityTexture table, texture
	sdListLevel();
	PositionEntity view\camera,0,25,-25
	RotateEntity  view\camera,45,0,0

	sdLoadLevelPreview(sdListNameLevel(sdListMeshCur))
Repeat ; Main Loop

	RenderWorld()
	sdFPS()
	
	Text GraphicsWidth()/2.0,GraphicsHeight()-30,"Chapaev v5.18 Demo (c)SDE_Group",True 
	Text GraphicsWidth()/2.0,20,"Selected Level: " + sdListNameLevel(sdListMeshCur),True
	Text GraphicsWidth()/2.0,40,"Use cursor key, ESC - Select",True
	Flip 1
	If KeyHit (205) Then
		sdListMeshCur=sdListMeshCur+1
		If sdListMeshCur>sdListNameLevelCount sdListMeshCur=0
		sdLoadLevelPreview(sdListNameLevel(sdListMeshCur))
	EndIf 
	If KeyHit (203) Then
		sdListMeshCur=sdListMeshCur-1
		If sdListMeshCur<0 sdListMeshCur=sdListNameLevelCount
		sdLoadLevelPreview(sdListNameLevel(sdListMeshCur))
	EndIf 
Until KeyHit(1)
	FreeEntity table
	For i=0 To 49
		If (sdListMesh(i)<>0) Then
			FreeEntity sdListMesh(i)
			sdListMesh(i)=0
		EndIf 
	Next
	sdLoadLevel(sdListNameLevel(sdListMeshCur))
End Function 

Function sdListLevel()
	; ReadDir/NextFile$/CloseDir example 
	
	; Define what folder to start with ... 
;	folder$="C:" 
	sdListNameLevelCount=-1
	; Open up the directory, and assign the handle to myDir 
	myDir=ReadDir(CurrentDir()) ;folder$) 
	
	; Let's loop forever until we run out of files/folders to list! 
	Repeat 
	; Assign the next entry in the folder to file$ 
	file$=NextFile$(myDir) 
	
	; If there isn't another one, let's exit this loop 
	If file$="" Then Exit 
	
	; Use FileType to determine if it is a folder (value 2) or a file and print results 
	;If FileType(folder$+"\"+file$) = 2 Then 
	;Print "Folder:" + file$
	
	If (Right( file$,3)="dat") Then 
		sdListNameLevelCount=sdListNameLevelCount+1
		sdListNameLevel(sdListNameLevelCount)=file$
	;Print "File:" + file$
	End If 
	Forever 
	
	; Properly close the open folder 
	CloseDir myDir 
	
	; We're done! 
	;Print "Done listing files!"
	
End Function 

;				If (objAll\colBody<>0) Then  
;					If (objAll\colBody<>tmpBody) Then; если партнер сменилс€?
;						For i=0 To cl-1
;							If (tmpBody<>pxContactGetBody(objAll\Body,i)) Then ;ищем его по остальным точкам
;								cl=0
;								Exit
;							EndIf 
;						Next 
;					EndIf 
;					objAll\colBodyTime=1
;				EndIf 
