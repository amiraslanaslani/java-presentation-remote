# Presentation Remote JS
A simple network-based remote control with Java. 
Currently, these can be controlled:
+ System volume. (increase, decrease, mute and unmute)
+ Up, Down, Left and Right buttons.
+ PageUp and PageDown buttons.
+ Home and End buttons.

## Usage
If we assume that the program is in the /Path/To/PresentationRemoteJS folder, you must use the following command to run server on `<PORT>` port number:
```
java -jar ./Path/To/PresentationRemoteJS [-p <PORT>]
```
Then you should see the output that says:
```
IP list of my system:
	<IP-ADDRESS-1>
	<IP-ADDRESS-2>
	.
	.
	.

Server is running on port <PORT> . . .
```
And now you have access to your system from `http://<IP-ADDRESS-X>:<PORT>` .
Also note that the default port is `8888`.

## Note:
Currently, volume controls (increase, decrease, mute and unmute) are only active for Gnu/Linux, MacOS and MS-Windows.
