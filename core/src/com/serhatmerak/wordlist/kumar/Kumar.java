package com.serhatmerak.wordlist.kumar;

import java.util.Arrays;
import java.util.Random;

public class Kumar {
	/*
	 * Malboro = 16tl
	 */
	private  int sigaraSayisi = 10;
	private  int min = 0, max = 10;
	private  int ucret = 1;
	
	private  int oyunSayisi = 1000;
	public   int karlar[] = new int[oyunSayisi];
	

	public Kumar() {
		for (int i = 0; i < oyunSayisi; i++) {
			int kar = kumarBir();
			karlar[i] = kar;
		}
		
		Arrays.sort(karlar);
		
		for(int i : karlar) {
			System.out.println(i);
		}
	}
	
	public  int kumarBir() {
		Random r=new Random(); 
		int kasadakiPara = 0;
		int sigaralar = sigaraSayisi;
		
		while(sigaralar > 0) {
			int musteriTahmin1 = r.nextInt(max);
			int musteriTahmin2 = r.nextInt(max);

			int makineSayisi1 = r.nextInt(max);
			int makineSayisi2 = r.nextInt(max);

			kasadakiPara += ucret;

			if((musteriTahmin1 == makineSayisi1 && musteriTahmin2 == makineSayisi2)
					|| 
					(musteriTahmin1 == makineSayisi2 && musteriTahmin2 == makineSayisi1)) {
				sigaralar--;
				System.out.println("Kazand�");
			}else if((musteriTahmin1 == makineSayisi1)
					||
					(musteriTahmin1 == makineSayisi2)
					||
					(musteriTahmin2 == makineSayisi1
					||
					(musteriTahmin2 == makineSayisi2))) {
				kasadakiPara -= ucret;
			}


			
			
		}
		
		System.out.println("Kasadaki para: " + kasadakiPara);
		System.out.println("Gider (Sigara 16tl): " + sigaraSayisi * 16);
		if(kasadakiPara > sigaraSayisi * 16)
			System.out.println(kasadakiPara - (sigaraSayisi * 16) + " tl K�r");
		else
			System.out.println(-kasadakiPara + (sigaraSayisi * 16) + " tl Zarar");
		
		int kar = kasadakiPara - (sigaraSayisi * 16);

		return kar;

	}

}
