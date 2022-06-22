# OpenGLES2 Learning Project

This project begun during the 3D lessons of my masters degree. 
It was tested with Android Studio emulator with API 30 and on mobile API 28. 

Controls : You can move in the scene with virtual (invisible) joysticks.
On the left side of the screen, we control the movement of the camera, the translation. On the right side of the screen, you can control the camera axis, the rotation. The rotation is limited to avoid turning the angle of view. Of course, it is possible and even recommended to use both fingers simultaneously to move and turn at the same time. Therefore, these controls are less effective on the emulator. To reset the user's position, just touch the screen with more than 2 fingers. This also resets the joysticks to the current position of the first 2 fingers.

The architecture is custom and was not part of the lesson.
It is inspired by Unity Component approach.
Thus, the objects I display are of the GameObject class. This class has several attributes:
- a transform "transform" of the Transform class
- a list of children objects
- a list of components of the Component class
- a parent object "parent".

The implemented components are :
- MeshFilter which is a mesh container
- MeshRenderer which displays the mesh of the MeshFilter according to the attributes of the material
- Light which allows to emit light

As for the shaders, I left all the ones I created. The most advanced is the ShadowShaders class with the shadow_frag and shadow_vert glsl. I have made a beginning of shadow implementation. Indeed, the directional light of the scene projects numbers in a certain area. For the moment this area is fixed, centered on the position of this light. The other lights do not cast shadows.

Pictures: 