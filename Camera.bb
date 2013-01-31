; Ver 5.19
;-=dyf=-
;	���������� � ������� ����� �������� <sdCamera>\sdProcessCamera(<...>)
;
;		��� ��������� ������ - ����� ��������� �������: <...>\status � <...>\nextStatus
; 1 - ������ ������������ �/� ������� ���������� � �������� ����� <...>\camPos � <...>\camRot.
; 2 - ��������� �������� ������ � ������� ���������
; 3 - �������� ������  ������ ������� <...>\targetPos �� ���������� <...>\camZoom
;---------------------------------------
Include "Vector3DMathLibrary.bb"

Type sdCamera
	Field camera
	Field camPos.Vector
	Field camRot.Vector
	Field targetPos.Vector
	Field camZoom#
	Field camForce
	Field status
	Field nextStatus
	Field lucky#
	Field level
	Field bonus
	Field steps ; ���
	Field white_count ; ���-�� ����� �� �����
End Type

Global view.sdCamera

Function sdResetCamera() ; �������� ��������� ������
	view\status=1:view\nextStatus=0
	Vector_Set(view\camPos,0,17,-17) ; ����������
	Vector_Set(view\camRot,45,0,0) ; ��������
	view\camZoom=10;Sqr (25*25+25*25)
	sdSelectObj=Null 
End Function
Function sdResetCameraCenter() 
	view\status=1:view\nextStatus=0
	Vector_Set(view\camPos,0,25,-5) ; ����������
	Vector_Set(view\camRot,80,0,0) ; ��������
	view\camZoom=Sqr(25*25+5*5)
	sdSelectObj=Null 
End Function
Function sdResetCameraDown()
	view\status=1:view\nextStatus=0
	Vector_Set(view\camPos,0,5,-25) ; ����������
	Vector_Set(view\camRot,20,0,0) ; ��������
	view\camZoom=Sqr(5*5+25*25)
	sdSelectObj=Null 
End Function

Function sdControlDebug(ent) ; ������� ���������� WASD
	If KeyDown(17) MoveEntity ent,0,0,1
	If KeyDown(31) MoveEntity ent,0,0,-1
	If KeyDown(30) MoveEntity ent,-1,0,0
	If KeyDown(32) MoveEntity ent,1,0,0
End Function

Function sdMouseLookFree(ent) ; ������� ������ �����
	mxspd#=MouseXSpeed()*0.05
	myspd#=MouseYSpeed()*0.05
	camForce=camForce-MouseZSpeed()
	If camForce>30 camForce=30
	If camForce<10 camForce=10
	MoveMouse GraphicsWidth()/2,GraphicsHeight()/2	
	 
	cameraPitch#=EntityPitch(ent)+myspd#
	
	If cameraPitch#<-85 Then cameraPitch#=-85 ; ����������� ��������, ����� ������ �� ��������� �� ������������� ����� � ����, � ��������������� ����� ����
	If cameraPitch#>85 Then cameraPitch#=85

	RotateEntity ent,cameraPitch#,EntityYaw(ent)-mxspd#,EntityRoll(ent)

End Function
Function sdMouseLookTarget() ; �������� ������  ������ ������� <...>\targetPos �� ���������� <...>\camZoom
	mxspd#=MouseXSpeed()*0.05
	myspd#=MouseYSpeed()*0.05
	view\camForce=view\camForce-MouseZSpeed()
	If view\camForce>30 view\camForce=30
	If view\camForce<10 view\camForce=10
	MoveMouse GraphicsWidth()/2,GraphicsHeight()/2	

	cameraPitch#=EntityPitch(view\camera)+myspd#
	
	If cameraPitch#<10 Then cameraPitch#=10 ; ����������� ��������, ����� ������ �� ��������� �� ������������� ����� � ����, � ��������������� ����� ����
	If cameraPitch#>85 Then cameraPitch#=85

	RotateEntity view\camera,cameraPitch#,EntityYaw(view\camera)-mxspd#,EntityRoll(view\camera)

	TFormVector 0,0,1,view\camera, 0
	tDirX# = TFormedX():	tDirY# = TFormedY():	tDirZ# = TFormedZ()
	tDirVector.Vector=Vector(tDirX,tDirY,tDirZ)
	Vector_MultiplyScalar(tDirVector,tDirVector,view\camZoom)
	tmpVector.Vector=Vector()
	Vector_Subtract(tmpVector,view\targetPos,tDirVector)
	Vector_PositionEntity(view\camera,tmpVector)
	;PositionEntity ent\camera,ent\targetVector\x-DirX*ent\camZoom,ent\targetVector\y-DirY*ent\camZoom,ent\targetVector\z-DirZ*ent\camZoom
End Function
Function sdCalcLookPoint() ; ��������� ����� ������ ��� ���� (<...>\targetPos ��� ������ ���� �����)
    tPoint.Vector=Vector()
    tNorm.Vector=Vector()
    Vector_Clone(tPoint,view\targetPos)
    Vector_Clone(tNorm,view\targetPos)
    tNorm\y=Sqr(tNorm\x*tNorm\x+tNorm\z*tNorm\z)/2.0;oaie auoiaa ia oaoeo
    Vector_Normalize(tNorm)
    Vector_MultiplyScalar(tNorm,tNorm,view\camZoom)
    Vector_Add(tPoint,tPoint,tNorm)
    Vector_Clone(view\camPos,tPoint)
    leng#=Vector_Magnitude(tPoint)
    aY#=-ATan2(-tPoint\x , -tPoint\z)
    Vector_SetY(view\camRot,aY)
    aXZ#=Sqr(tNorm\x*tNorm\x + tNorm\z*tNorm\z)
    aX#=ATan(tNorm\y / aXZ)
    Vector_SetX(view\camRot,aX)
End Function

Function sdMoveCamera() ; ����������� ����� ���.���������� � ������� � ������������� ��������� ������
	tCurCamPos.Vector=Vector()
	Vector_Set_Entity_Pos(view\camera,tCurCamPos) ; �������� ���.����-�� ������
	tmpVec.Vector=Vector(0,0,0)
	Vector_Subtract(tmpVec,view\camPos,tCurCamPos) ; ������� ����� �������� ���������� � �������
	leng#=Vector_Magnitude(tmpVec)
	;If (leng=0) Return
	Vector_Normalize(tmpVec)
	tCurCamRot.Vector=Vector()
	Vector_Set_Entity_Rot(view\camera,tCurCamRot) ; �������� ���. �������� ������
	tmpVecRot.Vector=Vector()
	tt#=0.5
	If (leng>0.55) Then
		Vector_AddTimeStep(tCurCamPos,tmpVec,tt) ; �������...
		stp%=leng/tt ; ����� ���������� �����
		Vector_Subtract(tmpVecRot,view\camRot,tCurCamRot) ; �������� ����������� �� ����
		Vector_DivideScalar(tmpVecRot,tmpVecRot,stp) ; �� stp �����
		Vector_Add(tCurCamRot,tCurCamRot,tmpVecRot) ; ���������...
	Else
		view\status=view\nextStatus
		view\nextStatus=0
		tCurCamPos=view\camPos
		Vector_Clone(tCurCamRot,view\camRot)
		MoveMouse GraphicsWidth()/2,GraphicsHeight()/2
	EndIf
	Vector_RotationEntity(view\camera,tCurCamRot) ; ����������� ��������� ���������
	Vector_PositionEntity (view\camera,tCurCamPos) ; ����������� ��������� ��������
;Delay 100
End Function

Function sdProcessCamera()
	; Start the selection process based on the value of '...' variable 
	Select view\status

	Case 1
		sdMoveCamera()
	Case 2
		sdMouseLookFree(view\camera)
	Case 3
		sdMouseLookTarget()
	Default
	End Select 
End Function 
;Function sdCreateCamera()

;Camera
view.sdCamera=New sdCamera
view\camera=CreateCamera()

CameraProjMode view\camera,1;; Set camera projection mode using mode value 
CameraZoom view\camera,1; Set camera zoom using zoom value

view\camPos=Vector()
view\camRot=Vector()
view\targetPos=Vector()
view\camZoom=10
view\camForce=17
view\status=1
view\lucky=Rnd(0.7,0.95)
view\bonus=100 ;  �� ������� ���������
view\steps=0
white_count=0

sdResetCamera()

