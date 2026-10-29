Черновые текстуры клубничного куста (16x16)

Стиль: тёмный ванильный, с клубничными признаками, чтобы куст
отличался от ванильного куста сладких ягод.

Новая композиция:
- age 0 — розетка с небольшим центральным побегом;
- age 1 — подросший куст с одним белым бутоном (переходная
  стадия между ростком и цветением);
- age 2 — цветущий куст: треугольник из шести белых цветков
  (1 снизу, 2 посередине, 3 сверху); рисунок прежний;
- age 3 — пять маленьких ягод 3x4: одна сверху, две по бокам,
  две снизу ближе к центру. У ягод зелёная чашечка, красное
  тело и розовый блик. Все ягоды меньше прежней крупной.

Файлы:
- strawberry_bush_stage0_new.png — росток (age 0)
- strawberry_bush_stage1_new.png — куст с бутоном (age 1)
- strawberry_bush_stage2_new.png — цветение (age 2)
- strawberry_bush_stage3_new.png — куст с ягодами (age 3)
- preview_10x.png — увеличенный предпросмотр (x10, шахматный фон = прозрачность)

Оригинальные текстуры в src/main/resources/assets/strawberry-mod/textures/block/ не изменялись.

Как поставить новые текстуры в игру:
1. Переименуйте файлы, убрав суффикс _new:
   strawberry_bush_stage0_new.png -> strawberry_bush_stage0.png
   strawberry_bush_stage1_new.png -> strawberry_bush_stage1.png
   strawberry_bush_stage2_new.png -> strawberry_bush_stage2.png
   strawberry_bush_stage3_new.png -> strawberry_bush_stage3.png
2. Скопируйте файлы с заменой в:
   src/main/resources/assets/strawberry-mod/textures/block/
3. Пересоберите мод: .\gradlew.bat build

Важно: новая последовательность уже внесена в мод:
- age 2 больше не пропускается, blockstate направляет его
  на модель strawberry_bush_stage2;
- куст растёт и ускоряется костной мукой по одной стадии;
- урожай собирается только на age 3, после сбора куст
  возвращается на age 1;
- в getShape() для age 2 используется GROWING_SHAPE.

Alt-версии:
- strawberry_bush_stage0_alt.png и strawberry_bush_stage3_alt.png —
  альтернативные варианты ростка и взрослого куста;
- для них уже сгенерированы ..._alt_tint.png и
  ..._alt_overlay.png, а также модели
  block/strawberry_bush_stage0_alt.json и
  block/strawberry_bush_stage3_alt.json;
- чтобы включить alt-вариант, поменяйте в
  blockstates/strawberry_bush.json модель у age=0 или age=3
  на соответствующую ..._alt.

Палитра (HEX):
Зелёные оттенки (от тёмного к светлому):
- #142614
- #1B3320
- #234A29
- #2C5C32
- #386F3C
- #478547
- #599855
- #6FA35A

Ягоды:
- тень:       #4A0A16
- тёмный:     #7A1020
- красный:    #A31628
- яркий:      #C92E44
- блик:       #E85A70

Цветок:
- лепесток:   #F2F2DE
- серединка:  #E8C84A

Чашечка ягоды:
- #6FA35A
