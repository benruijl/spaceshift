varying vec4 vertColor;
attribute float phi;

void main(){
    vec4 skewed = vec4(gl_Vertex.x + gl_Vertex.y * cos(phi), gl_Vertex.y * sin(phi), gl_Vertex.z, gl_Vertex.w);
    gl_Position = gl_ModelViewProjectionMatrix*skewed;
    vertColor = vec4(0.6, 0.3, 0.4, 1.0);
}