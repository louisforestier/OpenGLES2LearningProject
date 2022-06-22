# OpenGLES2 Learning Project

This project begun during the 3D lessons of my masters degree. 
It was tested with Android Studio emulator with API 30 and on mobile API 28. 

Controls : You can move in the scene with virtual (invisible) joysticks.

The architecture is custom and was not part of the lesson.
It is inspired by Unity Component approach.



La plus abouti est la classes ShadowShaders avec les glsl shadow_frag et shadow_vert.
J'y ai fait un début d'implantation des ombres. 
En effet, la lumière directionnelle de la scène projette des nombres dans une certaine zone.
Pour l'instant cette zone est fixe, centrée sur la position de cette lumière.
Les autres lumières ne projettent pas d'ombre.

