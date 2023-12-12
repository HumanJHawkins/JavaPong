# JavaPong
Pong via Java

Initially this was a test of ChatGPT's ability to form working Java code. An abridged log of prompts and results follows:

  - Please write a very simple Pong game in Java, that uses the computer mouse for paddle movement.
  - Good. This is simple just as I requested. Keeping the game simple for the user so it is still just a ball bouncing against a paddle, and without adding fancy colors or a user experience change, can you make it draw the movement more smoothly? Currently, the screen flickers a lot as motion happens in the game.
  - That is much better. Really good actually. Except that the ball only moves on the y-axis. So it never hits the paddle. Can you make it so the ball has horizontal movement?
  - Perfect. Are there any code best practices or structural improvements that could be made to this? I want it to be very simple from the user perspective, but very robust, using appropriate coding patterns and other best practices of coding.

At this point, the advice from GPT remained good, but the code was flawed. After some minor manual tweaks, I gave code with the revisions back to GPT and continued...

  - Please revert to the code below, and implement "Runnable".
  - Perfect. Now, please implement encapsulation, without also implementing seperation of concerns.

Some hallucination bugs crept in... GPT attempted to use a method it had not defined. Fixed this and continued.

  - Now, please implement seperation of concerns.

The seperation of concerns was OK, but in implementing, GPT lost double-buffering of output. But was able to restore double-buffering on being prompted to do so.

At this point, I attempted more than a dozen variations of prompts or sets of prompts to get ChatGPT to be able to display a splash screen AND still accept keyboard input for a "Press 'p' to play" message. It worked about 80% well enough in most answers, but GPT was never able to make it work reliably... And was not able to make the splash screen modal. So I had it revert to a text message for now.

The code also went off the rails in multiple ways, in most of GPT's attempts to handle my prompts. Assertions related to order of operations and attempts to use non-existant code were prevalent. It was clear that this was beyond GPT 3.5's capabilities. So I reduced the complexity of my requests:

  - With the following as a starting point (I input the simplest working code as a starting point), modify the code to wait for the press of a 'p' key to start. And to let the user know this is happening with the message "Press 'p' to Start" on screen.
  - Great. Make the text Blue and larger (maybe 24 point). Then, modify the code to go back into the waiting state on game over. Such  that pressing 'p' will start a new game.
  - Perfect. Now, make the "Press 'P' to Play" message be 1/3rd of the way from the top of the screen. And, get rid of the previous game over dialog.
  - Almost perfect. Can you make it so the game field is not reset until after the user presses 'p'. So they will be able to see the state of the game loss before starting again.

The result is the first commit here. I'll probably continue developing this as an example of a simple Java Swing program. All things considered, GPT had a fairly amazing success rate in this test of it's capabilities.
