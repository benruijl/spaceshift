uniform mat4 worldtrans; 

void main(){
    gl_Position = gl_ModelViewProjectionMatrix * worldtrans * gl_Vertex;
}