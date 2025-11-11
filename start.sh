#!/bin/bash

# Script pour démarrer l'application avec un profil spécifique

echo "🚀 OM Pay - Sélection du profil"
echo "================================"
echo ""
echo "Profils disponibles :"
echo "  1) dev  - Développement (create-drop, logs DEBUG)"
echo "  2) prod - Production (validate, logs WARN)"
echo ""

read -p "Choisissez un profil (1 ou 2) : " choice

case $choice in
  1)
    PROFILE="dev"
    echo ""
    echo "✅ Démarrage en mode DÉVELOPPEMENT"
    ;;
  2)
    PROFILE="prod"
    echo ""
    echo "✅ Démarrage en mode PRODUCTION"
    ;;
  *)
    echo "❌ Choix invalide. Utilisation du profil par défaut (dev)"
    PROFILE="dev"
    ;;
esac

echo ""
echo "📦 Compilation en cours..."
./mvnw clean compile -DskipTests

echo ""
echo "🎯 Profil actif : $PROFILE"
echo "🌐 Démarrage de l'application..."
echo ""

./mvnw spring-boot:run -Dspring-boot.run.profiles=$PROFILE