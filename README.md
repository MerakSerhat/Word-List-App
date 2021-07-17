## Word List App

- With this application, you can create a word list with the **words that you do not know** separated from a text or a file(even a complete e-book file). 
After you have entered the text or file to the application, it takes the words that you do not know and gets the definitions and example sentences from [Cambridge Dictionary](https://dictionary.cambridge.org/tr/ "Cambridge Dictionary") and creates a Word List.
- You can use **Word Tests** and **Study Cards** to learn these words. Once you learned a word, sign it as a known word so that you will not see that word again when you create a new Word List.
- Also if you want to see a little more about a word, you can find the **synonyms**, **antonyms** and plenty of **example sentences** with only one click.

## Development

- This application is based on [libGDX](https://libgdx.com/ "libGDX")  framework with java.
- [Jsoup](https://jsoup.org/) library used for getting information from web pages.
- [JNativeHook](https://github.com/kwhat/jnativehook "JNativeHook") library used for detecting some keyboard activities while this application is not focused (For beta 'Watching Mode')
- Also, **java sockets** were used for the same beta feature to connect this application with a mobile one.

#### beta features

1.  With 'Reader Mode',   you can immediately see the definition and example sentences when you copy a word to the clipboard.
	**Suggestion**: if you split your screen into two parts: 80% for the e-book, %20 for Word List App, you can easily read an English book and when you encounter an unknown word you can copy that word and see the definition within the seconds. (There is a button in the Reader Mode screen to resize the application automatically)

2. With 'Watching Mode', if you open a mobile application and connect your phone and desktop to the same wifi, after a few instructions, on your mobile phone, you can see the live English subtitles of a youtube video that you are currently watching from your desktop. When you stop the video from your desktop, subtitle flow from your mobile phone also stops.
The point of this feature is when you click on a word from your phone, immediately you can see the definitions and example sentences of that word. So, you can learn a new word while you are watching an English video on youtube without having to stop the video.
- [JNativeHook](https://github.com/kwhat/jnativehook "JNativeHook") library used for detecting some keyboard actions while you are watching to video. 
(For example, while you are watching a video from youtube when you press to space, the application detects it thanks to this library and sends a signal to the mobile application to stop the flow of subtitles. )



------------

- You can find the jar file inside of source codes.
- If you want to try the beta 'Watching Mode', write to me so that I can send you the mobile application.

