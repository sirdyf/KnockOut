; Ver 5.1
;Blitz3D
Graphics3D 1027,768,32,2
SetBuffer BackBuffer()

; PhysicX
pxCreateWorld(1, "key")	
pxSetGravity(0,-10,0)		
pxSDKSetParameter(0.01) ; Глубина проникновения 0.05 default

sdFont = LoadFont ("courier",20,False,False,False)
SetFont sdFont

Global sdFps, sdFpsC, sdFpsMs
Function sdFPS()
	sdFpsC = sdFpsC + 1
	If sdFpsMs<MilliSecs()
		sdFpsMs=MilliSecs()+1000 
		sdFps = sdFpsC
		sdFpsC = 0 
	EndIf
	Color 50,200,50
	Text 0,0,  "FPS: "+sdFps
End Function

;HidePointer ()