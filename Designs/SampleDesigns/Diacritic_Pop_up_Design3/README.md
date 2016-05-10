Pop-Up.
Each Key has associated with it, a mini keyboard xml.
So when pressed, it creates combinations of the letter with the Diacritic.

Say when letter k is pressed, Keyboard will another layout with forms...K,Ka,Ki,Kee....

				Advantage :-	Will make typing fast once user gets used to.

								Dynamically creates the combinations.(k+vowels----ka,ki...).
								
								Saves Space. As one doesn't need to permanently associate space with these combinations.So if a vowels and b number of consonants are there, ab-b space is saved.
								
								Instead of traditional keyboards,Like Inscript, where one will have to make k+ |  to make ka. This one will dynamically create 
								ka.
				
				
				Disadvantage :- As each key is associated with entire another xml layout which will pop-up, number of xml files to be maintained increases as language will increase.


		Reference

		http://developer.android.com/reference/android/inputmethodservice/Keyboard.Key.html#attr_android:popupKeyboard


				