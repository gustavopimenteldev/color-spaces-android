# 🎨 Color Spaces

**🌎 Select Language | Selecione o Idioma**  
[🇺🇸 English](#-about-the-project) | [🇧🇷 Português](#-sobre-o-projeto)

## 📌 About the Project
**Color Spaces** is an **Android app** that allows users to experiment with color theory through **palette mixing, light simulation, and a color guessing game**.

This project was built using **Jetpack Compose** and follows the **MVVM architecture**, making it a **modern, scalable, and maintainable** Android application.

I developed this app as a way to **deepen my knowledge of Android development**, color theory, and **UI/UX design**, while also exploring advanced concepts such as **custom views and graphics rendering**.

---

## ✨ Features
### 🎨 **Spaces**  
- Create a **color palette** by mixing three primary colors.
- Choose between **additive** (light mixing) and **subtractive** (pigment mixing) models.
- Save and load up to **10 custom palettes**.
- Click on any color in the hex grid to **select and store it**.

### 💡 **Lights**  
- Simulate how **colored materials behave under different light sources**.
- Adjust the **sphere’s color** and **light source colors** dynamically.
- Click anywhere on the sphere to **get the exact color** of that spot.

### 🎯 **Guess**  
- A **Wordle-inspired** game where you **guess the hex code** of a color in 6 tries.
- Each cell corresponds to **Red (R), Green (G), and Blue (B)** values.
- Feedback indicators:
  - ✅ **Green** → Correct value.
  - 🔵 **Blue** → Too low.
  - 🔴 **Red** → Too high.

---

## 🔧 Technologies Used
- **Jetpack Compose** - Modern UI toolkit for building native Android interfaces.
- **MVVM Architecture** - Ensures scalability and separation of concerns.
- **Kotlin** - Main programming language for development.
- **Android ViewModel & LiveData** - For state management and UI updates.
- **Canvas & Custom Views** - Used to create the **Hex Grid** and **Sphere Lighting simulations**.
- **SharedPreferences** - For saving and loading custom palettes.
- **Material 3 (M3)** - For a **modern, responsive UI**.
- **ProGuard / R8** - Optimized for **small APK size**.

---

## 🖌 Custom Color Picker
The **color picker** used in this app is a **modified version** of [KavehColorPicker](https://github.com/Mohammad3125/KavehColorPicker), which was customized to better integrate with the project.

---

## 📷 Screenshots
![Screenshot_2025-02-18-19-22-14-651_com example colorspaces](https://github.com/user-attachments/assets/2e5eae51-834d-46de-a33b-813c1218404c) | ![Screenshot_2025-02-18-19-22-20-047_com example colorspaces](https://github.com/user-attachments/assets/f9a5336c-fa4d-4360-abc6-a15574473c9f) | ![Screenshot_2025-02-18-19-22-30-294_com example colorspaces](https://github.com/user-attachments/assets/844a8f96-b9e8-43d3-9e90-6e494dd031cf) | ![Screenshot_2025-02-18-19-23-30-332_com example colorspaces](https://github.com/user-attachments/assets/d8ead8ec-5c55-4f52-befa-feac929ea8e4) | ![Screenshot_2025-02-18-19-23-42-942_com example colorspaces](https://github.com/user-attachments/assets/bb7cc9c4-8787-4be4-8179-fffdc1d17cb9) | ![Screenshot_2025-02-18-19-30-04-895_com example colorspaces](https://github.com/user-attachments/assets/695a272e-ddb1-47e1-9a07-6d43b353ade1) | ![Screenshot_2025-02-18-19-25-13-439_com example colorspaces](https://github.com/user-attachments/assets/e12e45d9-e822-4e39-8d81-afb4b5bf562d) | ![Screenshot_2025-02-18-19-25-10-940_com example colorspaces](https://github.com/user-attachments/assets/e5cd81f0-5098-4201-b29e-7f92be943ae9)

---

## 🚀 Getting Started
### **Clone the Repository**
```sh
git clone https://github.com/yourgithub/ColorSpaces.git
cd ColorSpaces
```

### **Build & Run**
Open in Android Studio.
Sync Gradle & build the project.
Run the app on an emulator or physical device.

---

### **📜 License **
This project is licensed under the MIT License. See the LICENSE file for details.



### **👨‍💻 Contact **
If you're interested in my work, feel free to connect:

📧 Email: gustavo.pimentel.dev@gmail.com
🔗 LinkedIn: www.linkedin.com/in/gustavo-pimentel-00b068210
🐙 GitHub: https://github.com/gustavopimenteldev/

[⬆ Back to Top](#-color-spaces) | [🇧🇷 Switch to Portuguese](#-sobre-o-projeto)


## 📌 Sobre o Projeto
**Color Spaces** é um **aplicativo Android** que permite aos usuários experimentarem a teoria das cores por meio de **mistura de paletas, simulação de luz e um jogo de adivinhação de cores**.

Este projeto foi desenvolvido utilizando **Jetpack Compose** e segue a arquitetura **MVVM**, tornando-o um aplicativo Android **moderno, escalável e mantével**.

Desenvolvi este aplicativo como uma forma de **aprofundar meus conhecimentos em desenvolvimento Android**, teoria das cores e **design UI/UX**, enquanto explorava conceitos avançados, como **visualização customizada e renderização de gráficos**.

---

## ✨ Funcionalidades
### 🎨 **Espaços**  
- Crie uma **paleta de cores** misturando três cores primárias.
- Escolha entre os modelos **aditivo** (mistura de luz) e **subtrativo** (mistura de pigmento).
- Salve e carregue até **10 paletas personalizadas**.
- Clique em qualquer cor na grade hexagonal para **selecioná-la e armazená-la**.

### 💡 **Luzes**  
- Simule como **materiais coloridos reagem sob diferentes fontes de luz**.
- Ajuste dinamicamente a **cor da esfera** e as **cores das fontes de luz**.
- Clique em qualquer lugar da esfera para **obter a cor exata** desse ponto.

### 🎯 **Adivinhação**  
- Um jogo inspirado no **Wordle**, onde você **tenta adivinhar o código hexadecimal** de uma cor em 6 tentativas.
- Cada célula corresponde aos valores **Vermelho (R), Verde (G) e Azul (B)**.
- Indicadores de feedback:
  - ✅ **Verde** → Valor correto.
  - 🔵 **Azul** → Muito baixo.
  - 🔴 **Vermelho** → Muito alto.

---

## 🔧 Tecnologias Utilizadas
- **Jetpack Compose** - Kit de ferramentas moderno para criar interfaces nativas no Android.
- **Arquitetura MVVM** - Garante escalabilidade e separação de responsabilidades.
- **Kotlin** - Linguagem principal utilizada no desenvolvimento.
- **Android ViewModel & LiveData** - Para gerenciamento de estado e atualizações de UI.
- **Canvas & Visualização Customizada** - Criados para a **Grade Hexagonal** e simulação de **Iluminação da Esfera**.
- **SharedPreferences** - Para salvar e carregar paletas personalizadas.
- **Material 3 (M3)** - Para uma **UI moderna e responsiva**.
- **ProGuard / R8** - Otimização para **redução do tamanho do APK**.

---

## 🖌 Seletor de Cores Personalizado
O **seletor de cores** usado neste aplicativo é uma **versão modificada** do [KavehColorPicker](https://github.com/Mohammad3125/KavehColorPicker), que foi customizado para melhor integração com o projeto.

---

## 📷 Capturas de Tela
![Screenshot_2025-02-18-19-22-14-651_com example colorspaces](https://github.com/user-attachments/assets/2e5eae51-834d-46de-a33b-813c1218404c) | ![Screenshot_2025-02-18-19-22-20-047_com example colorspaces](https://github.com/user-attachments/assets/f9a5336c-fa4d-4360-abc6-a15574473c9f) | ![Screenshot_2025-02-18-19-22-30-294_com example colorspaces](https://github.com/user-attachments/assets/844a8f96-b9e8-43d3-9e90-6e494dd031cf) | ![Screenshot_2025-02-18-19-23-30-332_com example colorspaces](https://github.com/user-attachments/assets/d8ead8ec-5c55-4f52-befa-feac929ea8e4) | ![Screenshot_2025-02-18-19-23-42-942_com example colorspaces](https://github.com/user-attachments/assets/bb7cc9c4-8787-4be4-8179-fffdc1d17cb9) | ![Screenshot_2025-02-18-19-30-04-895_com example colorspaces](https://github.com/user-attachments/assets/695a272e-ddb1-47e1-9a07-6d43b353ade1) | ![Screenshot_2025-02-18-19-25-13-439_com example colorspaces](https://github.com/user-attachments/assets/e12e45d9-e822-4e39-8d81-afb4b5bf562d) | ![Screenshot_2025-02-18-19-25-10-940_com example colorspaces](https://github.com/user-attachments/assets/e5cd81f0-5098-4201-b29e-7f92be943ae9)

---

## 🚀 Como Começar
### **Clonar o Repositório**
```sh
 git clone https://github.com/yourgithub/ColorSpaces.git
 cd ColorSpaces
```

### **Compilar e Executar**
Abra no Android Studio.  
Sincronize o Gradle e compile o projeto.  
Execute o app em um emulador ou dispositivo físico.

---

### **📜 Licença**
Este projeto está licenciado sob a MIT License. Consulte o arquivo LICENSE para mais detalhes.


### **👨‍💻 Contato**
Se estiver interessado no meu trabalho, sinta-se à vontade para entrar em contato:

📧 Email: gustavo.pimentel.dev@gmail.com  
🔗 LinkedIn: www.linkedin.com/in/gustavo-pimentel-00b068210  
🐙 GitHub: https://github.com/gustavopimenteldev  

[⬆ Voltar ao Topo](#-sobre-o-projeto) | [🇺🇸 Mudar para Inglês](#-about-the-project)
