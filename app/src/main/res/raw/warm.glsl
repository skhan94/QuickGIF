varying highp vec2 textureCoordinate;
precision highp float; 
  
uniform sampler2D inputImageTexture;
uniform sampler2D curve;
uniform sampler2D greyFrame;
uniform sampler2D layerImage;

void main()
{ 
	lowp vec4 textureColor; 
	vec4 greyColor;
	vec4 layerColor;
	
	float xCoordinate = textureCoordinate.x;
	float yCoordinate = textureCoordinate.y;
	
	highp float redCurveValue;
	highp float greenCurveValue; 
	highp float blueCurveValue;

	textureColor = texture2D( inputImageTexture, vec2(xCoordinate, yCoordinate));

	greyColor = texture2D(greyFrame, vec2(xCoordinate, yCoordinate));
	layerColor = texture2D(layerImage, vec2(xCoordinate, yCoordinate));

	// step1 curve
	redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).r;
	greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).g;
	blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).b;

    // step2 curve with mask
	textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

	redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).a;
	greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).a;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).a;

	lowp vec4 textureColor2 = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

	// step3 screen with 60%
	lowp vec4 base = vec4(textureColor.r, textureColor.g, textureColor.b, 1.0);
	lowp vec4 overlayer = vec4(layerColor.r, layerColor.g, layerColor.b, 1.0);

    // screen blending
	/*textureColor = 1.0 - ((1.0 - base) * (1.0 -overlayer));
	textureColor = (textureColor - base) * 1.5 + base;*/
	/*if (base < 0.5) {
        result = 2.0 * base * blend;
    } else {
        result = vec4(1.0) - 2.0 * (vec4(1.0) - blend) * (vec4(1.0) - base);
    }*/
   // blendNormal(base, blend) * opacity + base * (1.0 - opacity));
   // textureColor = overlayer + base * (1.0 - overlayer);
    //	textureColor =  textureColor * 1.5 + base;
//	textureColor =   1.0 - (base * overlayer);

    textureColor = 1.0 - ((1.0 - base) * (1.0 - overlayer));
	textureColor = (textureColor - base) * (1.6 + base) * (greycolor);

//textureColor=overlayer;
	redCurveValue = texture2D(curve, vec2(textureColor.r, 1.0)).r;
	greenCurveValue = texture2D(curve, vec2(textureColor.g, 1.0)).g;
	blueCurveValue = texture2D(curve, vec2(textureColor.b, 1.0)).b;
	textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

	gl_FragColor = vec4(textureColor.r,textureColor.g,textureColor.b,greyColor.a);
}

