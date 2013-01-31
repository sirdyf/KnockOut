;Ver 5.15
Function sdProcessClickMouse(ent.sdCamera)
	If MouseHit(1) = True Then
		sdClickLeftButton(ent)
	EndIf
	If MouseHit(2) = True Then
		sdClickRightButton(ent)
	EndIf
	If MouseHit (3) Then
		sdClickMiddleButton(ent)
	End If
	ent\camForce=ent\camForce-MouseZSpeed()
	If ent\camForce<10 ent\camForce=10
	If ent\camForce>30 ent\camForce=30
End Function 
Function sdSelectClickObj(ent.sdCamera)
	CameraPick(ent\camera, MouseX(), MouseY())
	SelectMesh = PickedEntity()
	sdSelectObj=Null 
	For obj.sdWhite = Each sdWhite
		If obj\saska\Mesh = SelectMesh Then
			sdSelectObj = obj\saska
		EndIf 
	Next
End Function 
Function sdClickLeftButton(ent.sdCamera)
	sdSelectClickObj(ent)
	If ent\status=0 Then
		If (sdSelectObj <> Null ) Then
			ent\status=1
			ent\nextStatus=3
			Vector_Set_Entity_Pos(sdSelectObj\Mesh,ent\targetPos)
			sdCalcLookPoint()
		EndIf
	ElseIf ent\status=3 Then
	;----------------
		TFormVector 0,0,1,ent\camera, 0
		DirX# = TFormedX():	DirY# = TFormedY():	DirZ# = TFormedZ()
		l#=Sqr (DirX*DirX+DirZ*DirZ)
		nDirX#=DirX#/l#
		nDirZ#=DirZ#/l#

		
		minValue=ent\camForce*ent\lucky
		frc=Rnd(minValue,ent\camForce)
		
		pxBodyAddForceAtPos sdSelectObj\Body, nDirX*ent\camForce,0,nDirZ*frc, ent\targetPos\x, ent\targetPos\y, ent\targetPos\z,1
		sdWatchObj=sdSelectObj
		ent\lucky=Rnd(0.7,0.95)
		ent\camForce=frc
		
		;bonus
		If  (sdSelectObj\used=0) Then
			sdSelectObj\used=1
			ent\bonus=ent\bonus-30
		EndIf
		ent\bonus=ent\bonus-10

		sdStep()
		
		sdResetCamera()
	;---------------
	End If
End Function
Function sdClickRightButton(ent.sdCamera)
	sdResetCamera()
End Function 

Function sdClickMiddleButton(ent.sdCamera)
	;sdLoadLevel() ;sdResetLevel(ent)
End Function 
