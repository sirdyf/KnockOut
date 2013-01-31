;Ver 5.17

Global sdMaterialMeshDefault = 0
Global sdMaterialBodyDefault = 0

Type sdObject
    Field Mesh
    Field Body
	Field used ; была ли эта шашка использована (исп-с€ пока только дл€ белых)
	Field drop ; дл€ подсчета бонусов за сбитие с доски(если 1 - бонус за нее уже начислен. ƒл€ белых и черных)
	Field frost% ; заморожена на это кол-во ходов. ѕока только дл€ белых.
	Field drop_step ; на каком шаге сброшена с доски. ƒл€ всех типов шашек.
	Field colBody
	Field colBodyTime
End Type

Type sdWhite
    Field saska.sdObject
End Type

Type sdBlack
    Field saska.sdObject
    Field MirrorMesh
End Type

Type sdNeutral
    Field saska.sdObject
End Type

Type sdMagnet
	Field Mesh
	Field Magnet
End Type 

Type sdMoved
	Field saska.sdObject
	Field firstPos.Vector
	Field secondPos.Vector
	Field firstMesh
	Field secondMesh
	Field force#
End Type

Function sdObjectUpdate()
    For obj.sdObject = Each sdObject
		pxBodySetEntity obj\Mesh, obj\Body
    Next
End Function

Function sdCreateCylinder.sdObject(PosX#=0, PosY#=0, PosZ#=0, Radius#=1, Height#=1, Segment=16, Mass#=1, MaterialMesh=0, MaterialBody=0)

	obj.sdObject		= New sdObject
	obj\Mesh      		= CreateCylinder(Segment)
	ScaleEntity obj\Mesh, Radius, Height, Radius

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

	Return obj

End Function

Function sdCreateBox.sdObject(PosX#=0, PosY#=0, PosZ#=0, SizeX#=1, SizeY#=1, SizeZ#=1, Mass#=1, MaterialMesh=0, MaterialBody=0)
	obj.sdObject		= New sdObject
	obj\Mesh      		= CreateCube()
	ScaleEntity obj\Mesh, SizeX, SizeY, SizeZ

	If MaterialMesh Then
		PaintEntity obj\Mesh, MaterialMesh
	Else
		PaintEntity obj\Mesh, sdMaterialMeshDefault
	EndIf
	
	obj\Body = pxBodyCreateCube(SizeX, SizeY, SizeZ,  Mass)
	If MaterialBody Then
		pxMaterialSetToBody(obj\Body, MaterialBody)
	Else
		pxMaterialSetToBody(obj\Body, sdMaterialBodyDefault)
	EndIf
	
	pxBodySetPosition obj\Body, PosX, PosY, PosZ
	pxBodySetEntity obj\Mesh, obj\body

	Return obj

End Function

;Function sdPickedObject.sdObject(Cam)
;	ray = pxCreateRay()
;	pxRaySetPosition(ray, EntityX(Cam,1), EntityY(Cam,1),EntityZ(Cam,1))
;	TFormVector 0,0,1,Cam, 0
;	DirX# = TFormedX()
;	DirY# = TFormedY()
;	DirZ# = TFormedZ()
;	pxRaySetDir(ray,DirX,DirY,DirZ)
;	Body = pxRayGetBody(ray,0)
;
;    For obj.sdObject = Each sdObject
;		If obj\Body = Body Then Return obj
;	Next
;
;	Return Null
;End Function
